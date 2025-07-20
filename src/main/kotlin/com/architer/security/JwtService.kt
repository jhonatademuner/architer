package com.architer.security

import com.architer.config.property.JwtProperties
import com.architer.domain.user.User
import com.architer.repository.user.UserRepository
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

    fun generateAccessToken(user: User): String {
        return generateToken(user, "access", jwtProperties.accessExpirationMs)
    }

    fun generateRefreshToken(user: User): String {
        return generateToken(user, "refresh", jwtProperties.refreshExpirationMs)
    }

    fun generateToken(user: User, type: String, expirationMs: Long): String {
        val claims = mutableMapOf<String, Any>(
            "type" to type,
            "isAdmin" to user.isAdmin
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

    fun extractUserId(token: String): String =
        extractClaim(token) { it.subject }
            ?: throw IllegalArgumentException("User ID could not be extracted from token")

    fun extractUser(token: String): User? {
        val userId = extractUserId(token)

        val user = userRepository.findByIdAndIsActiveTrue(UUID.fromString(userId))
        if (user == null) {
            logger.warn { "Active user not found for userId=$userId" }
        }

        return user
    }

    fun extractJti(token: String): String? =
        extractClaim(token) { it.id }

    fun isAdmin(token: String): Boolean =
        extractClaim(token) { it["isAdmin"]?.toString()?.lowercase() == "true" } ?: false

    fun isRefreshToken(token: String): Boolean =
        extractClaim(token) { it["type"] == "refresh" } ?: false

    fun isAccessToken(token: String): Boolean =
        extractClaim(token) { it["type"] == "access" } ?: false

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
            null
        } catch (ex: JwtException) {
            logger.warn { "Invalid JWT: ${ex.message}" }
            null
        } catch (ex: Exception) {
            logger.error { "Unexpected error parsing token: ${ex.message}" }
            null
        }
    }
}
