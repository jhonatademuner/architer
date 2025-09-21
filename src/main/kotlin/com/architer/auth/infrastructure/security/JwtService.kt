package com.architer.auth.infrastructure.security

import com.architer.auth.domain.model.TokenType
import com.architer.auth.infrastructure.security.JwtProperties
import com.architer.user.domain.repository.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class JwtService(
    private val jwtProperties: JwtProperties,
    private val userRepository: UserRepository
) {

    private val signingKey: Key = Keys.hmacShaKeyFor(
        Decoders.BASE64.decode(jwtProperties.secretKey)
    )

    fun generateToken(userId: UUID, type: TokenType, expirationMs: Long): String {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found with ID: $userId") }
        val claims = mutableMapOf<String, Any>(
            "type" to type,
            "isAdmin" to user.isAdmin()
            // other custom claims
        )
        val now = Date()
        val expiry = Date(now.time + expirationMs)
        val jti = UUID.randomUUID().toString()

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(user.id.toString())
            .setIssuer(jwtProperties.issuer)
            .setAudience(jwtProperties.audience)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .setId(jti)
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()
    }


    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val claims = extractAllClaims(token) ?: return false
        return claims.subject == (userDetails as CustomUserDetails).getId().toString() &&
                !isTokenExpired(token) &&
                claims.issuer == jwtProperties.issuer &&
                claims.audience == jwtProperties.audience
    }

    fun extractUserId(token: String): UUID =
        extractClaim(token) { UUID.fromString(it.subject) }
            ?: throw IllegalArgumentException("User ID could not be extracted from token")

    fun extractJti(token: String): String? =
        extractClaim(token) { it.id }

    fun extractTokenType(token: String): TokenType {
        val rawType = extractClaim(token) { it["type"] as? String }
            ?: throw IllegalArgumentException("Token type could not be extracted from token")
        return TokenType.valueOf(rawType)
    }

    private fun isTokenExpired(token: String): Boolean =
        extractClaim(token) { it.expiration.before(Date()) } ?: true

    fun <T> extractClaim(token: String, resolver: (Claims) -> T?): T? {
        val claims = extractAllClaims(token) ?: return null
        return try {
            resolver(claims)
        } catch (ex: Exception) {
            logger.warn { "Error extracting claim: ${ex.message}" }
            null
        }
    }

    private fun extractAllClaims(token: String): Claims? {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .body

            if (claims.issuer != jwtProperties.issuer || claims.audience != jwtProperties.audience) {
                logger.warn { "Invalid issuer or audience: ${claims.issuer} / ${claims.audience}" }
                return null
            }

            claims
        } catch (ex: ExpiredJwtException) {
            logger.warn { "JWT expired: ${ex.message}" }
            val claims = ex.claims
            if (claims.issuer != jwtProperties.issuer || claims.audience != jwtProperties.audience) {
                logger.warn { "Invalid issuer or audience in expired token: ${claims.issuer} / ${claims.audience}" }
                return null
            }
            claims
        } catch (ex: JwtException) {
            logger.warn { "Invalid JWT: ${ex.message}" }
            null
        } catch (ex: Exception) {
            logger.error { "Unexpected error parsing token: ${ex.message}" }
            null
        }
    }
}
