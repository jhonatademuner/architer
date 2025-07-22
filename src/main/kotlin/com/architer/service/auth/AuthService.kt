package com.architer.service.auth

import com.architer.assembler.user.UserAssembler
import com.architer.config.property.JwtProperties
import com.architer.domain.auth.RevokedToken
import com.architer.domain.user.User
import com.architer.dto.auth.JwtTokenDTO
import com.architer.dto.auth.UserRegisterDTO
import com.architer.dto.user.UserDTO
import com.architer.repository.auth.RevokedTokenRepository
import com.architer.repository.user.UserRepository
import com.architer.security.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@Service
class AuthService(
    private val jwtService: JwtService,
    private val authManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val revokedTokenRepository: RevokedTokenRepository,
    private val userAssembler: UserAssembler,
    private val jwtProperties: JwtProperties,
    private val passwordEncoder: PasswordEncoder
) {

    fun login(email: String, password: String): JwtTokenDTO {
        var user: User? = userRepository.findByEmailAndIsActiveTrue(email)
        if (user == null) user = userRepository.findByEmailAndIsActiveTrue(email)
            ?: throw BadCredentialsException("Invalid credentials")

        val authentication = authManager.authenticate(
            UsernamePasswordAuthenticationToken(user.email, password)
        )
        if (!authentication.isAuthenticated) throw BadCredentialsException("Invalid credentials")

        val accessToken = jwtService.generateToken(user, "access", jwtProperties.accessExpirationMs)
        val refreshToken = jwtService.generateToken(user, "refresh", jwtProperties.refreshExpirationMs)

        return JwtTokenDTO(accessToken, refreshToken)
    }

    fun register(dto: UserRegisterDTO): UserDTO {
        validatePasswordStrength(dto.password)
        if (userRepository.existsByEmail(dto.email)) {
            throw IllegalArgumentException("User with username or email already exists")
        }
        val user = userAssembler.toEntity(dto).apply {
            password = passwordEncoder.encode(dto.password)
        }
        return userAssembler.toDto(userRepository.save(user))
    }

    fun refreshToken(refreshToken: String, accessToken: String): JwtTokenDTO {
        val jti = jwtService.extractJti(refreshToken) ?: throw IllegalArgumentException("Invalid token")

        // Check for token reuse
        if (revokedTokenRepository.existsByJti(jti)) {
            val userId = jwtService.extractUserId(refreshToken)
            revokeAllTokensForUser(UUID.fromString(userId))
            throw SecurityException("Token reuse detected. All sessions revoked.")
        }

        // Validate before revocation
        if (!jwtService.isRefreshToken(refreshToken)) throw IllegalArgumentException("Invalid refresh token")
        val user = jwtService.extractUser(refreshToken) ?: throw IllegalArgumentException("Invalid token")

        revokeToken(refreshToken)
        revokeToken(accessToken)
        val isRefresh = jwtService.isRefreshToken(refreshToken)
        if (!isRefresh) throw IllegalArgumentException("Invalid refresh token")

        // Generate new tokens
        val newAccessToken = jwtService.generateToken(user, "access", jwtProperties.accessExpirationMs)
        val newRefreshToken = jwtService.generateToken(user, "refresh", jwtProperties.refreshExpirationMs)

        return JwtTokenDTO(newAccessToken, newRefreshToken)
    }

    fun revokeToken(token: String) {
        val userId = jwtService.extractUserId(token)
        val jti = jwtService.extractJti(token) ?: return
        val tokenType = jwtService.extractClaim(token) { it["type"] as? String } ?: return
        val expiration = jwtService.extractClaim(token) { it.expiration }
            ?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime() ?: return

        if (!revokedTokenRepository.existsById(jti)) {
            revokedTokenRepository.save(RevokedToken(jti, UUID.fromString(userId), tokenType, expiration))
        }
    }

    fun verifyPassword(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }

    fun validatePasswordStrength(password: String) {
        val minLength = 8
        val hasUppercase = Regex("[A-Z]")
        val hasDigit = Regex("\\d")
        val hasSpecial = Regex("[!@#\$%^&*()\\-+=\\[\\]{};:'\"\\\\|,.<>/?]")

        when {
            password.length < minLength ->
                throw IllegalArgumentException("Password must be at least $minLength characters long")
            !hasUppercase.containsMatchIn(password) ->
                throw IllegalArgumentException("Password must contain at least one uppercase letter")
            !hasDigit.containsMatchIn(password) ->
                throw IllegalArgumentException("Password must contain at least one number")
            !hasSpecial.containsMatchIn(password) ->
                throw IllegalArgumentException("Password must contain at least one special character")
        }
    }

    fun revokeAllTokensForUser(userId: UUID) {
        revokedTokenRepository.deleteAllByUserId(userId)

        val markerToken = RevokedToken(
            jti = "mass-revoke-$userId-${System.currentTimeMillis()}",
            userId = userId,
            tokenType = "mass_revoke",
            expiresAt = LocalDateTime.now().plusYears(10)
        )
        revokedTokenRepository.save(markerToken)
    }

}
