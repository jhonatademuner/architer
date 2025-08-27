package com.architer.auth.infrastructure.persistence

import com.architer.auth.domain.model.RevokedToken
import com.architer.auth.domain.repository.RevokedTokenRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class RevokedTokenRepositoryAdapter(
    private val jpaRepository: JpaRevokedTokenRepository
): RevokedTokenRepository {
    override fun existsByJti(jti: String): Boolean = jpaRepository.existsByJti(jti)
    override fun deleteAllByUserId(userId: UUID) : Unit = jpaRepository.deleteAllByUserId(userId)
    override fun save(revokedToken: RevokedToken): RevokedToken = jpaRepository.save(revokedToken)
}