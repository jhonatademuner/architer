package com.architer.auth.domain.repository

import com.architer.auth.domain.model.RevokedToken
import java.util.UUID

interface RevokedTokenRepository {
    fun existsByJti(jti: String): Boolean
    fun deleteAllByUserId(userId: UUID) : Unit
    fun save(revokedToken: RevokedToken): RevokedToken
}