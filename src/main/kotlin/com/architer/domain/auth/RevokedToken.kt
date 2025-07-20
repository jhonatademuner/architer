package com.architer.domain.auth

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID


@Entity
@Table(name = "revoked_tokens")
data class RevokedToken(
    @Id
    val jti: String,
    val userId: UUID,
    val tokenType: String,
    val expiresAt: LocalDateTime
)

