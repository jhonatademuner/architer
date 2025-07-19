package com.architer.config.filter

import com.architer.repository.auth.RevokedTokenRepository
import com.architer.security.CustomUserDetails
import com.architer.security.JwtService
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

private val logger = KotlinLogging.logger {}

@Order(2)
@Component
class JwtFilter(
    private val jwtService: JwtService,
    private val revokedTokenRepository: RevokedTokenRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        val jwtToken = authHeader?.takeIf { it.startsWith("Bearer ") }?.substringAfter("Bearer ")?.trim()

        if (jwtToken != null && SecurityContextHolder.getContext().authentication == null) {
            try {
                // Revocation check
                jwtService.extractJti(jwtToken)?.let { jti ->
                    if (revokedTokenRepository.existsById(jti)) {
                        logger.warn { "Token revoked: $jti" }
                        throw BadCredentialsException("Token has been revoked")
                    }
                }

                // Token type validation
                if (jwtService.isRefreshToken(jwtToken) &&
                    request.servletPath != "/api/v1/auth/refresh") {
                    throw BadCredentialsException("Refresh token used as access token")
                }

                // User extraction and validation
                val user = jwtService.extractUser(jwtToken)
                    ?: throw BadCredentialsException("Invalid token")
                val userDetails = CustomUserDetails(user)

                if (jwtService.isTokenValid(jwtToken, CustomUserDetails(user))) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        CustomUserDetails(user), null, userDetails.authorities
                    ).apply {
                        details = WebAuthenticationDetailsSource().buildDetails(request)
                    }
                    SecurityContextHolder.getContext().authentication = authToken
                }
            } catch (ex: Exception) {
                SecurityContextHolder.clearContext()
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials")
                return
            }
        }
        filterChain.doFilter(request, response)
    }
}
