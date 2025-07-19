package com.architer.repository.auth

import com.architer.domain.auth.RevokedToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface RevokedTokenRepository : JpaRepository<RevokedToken, String> {
    fun existsByJti(jti: String): Boolean

    @Modifying
    @Query("DELETE FROM RevokedToken r WHERE r.userId = :userId")
    fun deleteAllByUserId(userId: UUID)
}
