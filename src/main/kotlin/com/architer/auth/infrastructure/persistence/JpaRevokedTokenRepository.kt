package com.architer.auth.infrastructure.persistence

import com.architer.auth.domain.model.RevokedToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface JpaRevokedTokenRepository: JpaRepository<RevokedToken, UUID> {
    fun existsByJti(jti: String): Boolean

    @Modifying
    @Query("DELETE FROM RevokedToken r WHERE r.userId = :userId")
    fun deleteAllByUserId(userId: UUID)
}