package com.architer.auth.domain.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "revoked_tokens")
@EntityListeners(AuditingEntityListener::class)
data class RevokedToken(

    @Id
    @Column(name = "jti", nullable = false, updatable = false)
    var jti: String,

    @Column(name = "user_id", nullable = false, updatable = false)
    var userId: UUID,

    @Column(name = "token_type", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    var tokenType: TokenType,

    @Column(name = "expires_at", nullable = false, updatable = false)
    var expiresAt: LocalDateTime,

    @CreatedDate
    @Column(name = "revoked_at", nullable = false, updatable = false)
    var revokedAt: LocalDateTime? = null,
)