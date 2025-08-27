package com.architer.auth.application

import com.architer.auth.domain.model.RevokedToken
import com.architer.auth.domain.model.TokenType
import com.architer.auth.domain.repository.RevokedTokenRepository
import com.architer.auth.infrastructure.security.JwtProperties
import com.architer.auth.infrastructure.security.JwtService
import com.architer.auth.presentation.dto.AuthResponse
import com.architer.auth.presentation.dto.LoginRequest
import com.architer.shared.exception.BadCredentialsException
import com.architer.user.application.UserService
import com.architer.user.presentation.dto.UserCreateRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@Service
class AuthService(
    private val jwtProperties: JwtProperties,
    private val authManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtService: JwtService,
    private val revokedTokenRepository: RevokedTokenRepository,
) {
    @Transactional
    fun register(request: UserCreateRequest): AuthResponse {
        if (userService.existsByEmail(request.email)) {
            throw IllegalArgumentException("User with email already exists")
        }
        userService.create(request)
        val loginRequest = LoginRequest(request.email, request.password)
        return login(loginRequest)
    }

    fun login(request: LoginRequest): AuthResponse {
        val user = userService.findByEmail(request.email)

        val authentication = authManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        if (!authentication.isAuthenticated) throw BadCredentialsException("Invalid credentials")

        val accessToken = jwtService.generateToken(user.id, TokenType.ACCESS, jwtProperties.accessExpirationMs)
        val refreshToken = jwtService.generateToken(user.id, TokenType.REFRESH, jwtProperties.refreshExpirationMs)

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    fun logout(accessToken: String) {
        revokeToken(accessToken)
    }

    fun refreshToken(refreshToken: String, accessToken: String): AuthResponse {
        // Validate before revocation
        if (jwtService.extractTokenType(refreshToken) != TokenType.REFRESH) throw IllegalArgumentException("Invalid refresh token")
        val jti = jwtService.extractJti(refreshToken) ?: throw IllegalArgumentException("Invalid token")
        if (revokedTokenRepository.existsByJti(jti)) {
            val userId = jwtService.extractUserId(refreshToken)
            revokeAllTokensForUser(userId)
            throw SecurityException("Token reuse detected. All sessions revoked.")
        }
        val userId = jwtService.extractUserId(refreshToken)

        revokeToken(refreshToken)
        revokeToken(accessToken)

        // Generate new tokens
        val newAccessToken = jwtService.generateToken(userId, TokenType.ACCESS, jwtProperties.accessExpirationMs)
        val newRefreshToken = jwtService.generateToken(userId, TokenType.REFRESH, jwtProperties.refreshExpirationMs)
        return AuthResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    private fun revokeToken(accessToken: String) {
        val userId = jwtService.extractUserId(accessToken)
        val jti = jwtService.extractJti(accessToken) ?: return
        val tokenType = jwtService.extractTokenType(accessToken)
        val expiresAt = jwtService.extractClaim(accessToken) { it.expiration }
            ?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime() ?: return

        if (!revokedTokenRepository.existsByJti(jti)) {
            val revokedToken = RevokedToken(
                jti = jti,
                userId = userId,
                tokenType = tokenType,
                expiresAt = expiresAt
            )
            revokedTokenRepository.save(revokedToken)
        }
    }

    private fun revokeAllTokensForUser(userId: UUID) {
        revokedTokenRepository.deleteAllByUserId(userId)

        val markerToken = RevokedToken(
            jti = "mass-revoke-$userId",
            userId = userId,
            tokenType = TokenType.MASS_REVOKE,
            expiresAt = LocalDateTime.now().plusYears(10)
        )
        revokedTokenRepository.save(markerToken)
    }
}