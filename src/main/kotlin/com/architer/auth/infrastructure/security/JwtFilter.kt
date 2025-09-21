package com.architer.auth.infrastructure.security

import com.architer.auth.domain.model.TokenType
import com.architer.auth.domain.repository.RevokedTokenRepository
import com.architer.user.domain.repository.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@Order(2)
@Component
class JwtFilter(
    private val jwtService: JwtService,
    private val revokedTokenRepository: RevokedTokenRepository,
    private val userRepository: UserRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        val jwtToken = authHeader?.takeIf { it.startsWith("Bearer ") }?.substringAfter("Bearer ")?.trim()

        if (jwtToken != null && SecurityContextHolder.getContext().authentication == null && !request.servletPath.contains("/auth/refresh")) {
            try {
                // Revocation check
                jwtService.extractJti(jwtToken)?.let { jti ->
                    if (revokedTokenRepository.existsByJti(jti)) {
                        logger.warn { "Token revoked: $jti" }
                        throw BadCredentialsException("Token has been revoked")
                    }
                }

                // Token type validation
                if (jwtService.extractTokenType(jwtToken) == TokenType.REFRESH) {
                    throw BadCredentialsException("Refresh token used as access token")
                }

                // User extraction and validation
                val userId = jwtService.extractUserId(jwtToken)
                val user = userRepository.findById(userId)
                    .orElseThrow { BadCredentialsException("User not found with ID: $userId") }
                val userDetails = CustomUserDetails(user)

                if (jwtService.isTokenValid(jwtToken, CustomUserDetails(user))) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        CustomUserDetails(user), null, userDetails.authorities
                    ).apply {
                        details = WebAuthenticationDetailsSource().buildDetails(request)
                    }
                    SecurityContextHolder.getContext().authentication = authToken
                } else {
                    throw BadCredentialsException("Token is invalid or expired")
                }
            } catch (ex: Exception) {
                SecurityContextHolder.clearContext()
                response.status = HttpStatus.UNAUTHORIZED.value()
                response.contentType = "application/json"
                response.writer.write(
                    """
                    {
                        "timestamp": "${LocalDateTime.now()}",
                        "status": 401,
                        "error": "Unauthorized",
                        "message": "${ex.message}",
                        "path": "${request.requestURI}"
                    }
                    """.trimIndent()
                )
                return
            }
        }
        filterChain.doFilter(request, response)
    }
}
