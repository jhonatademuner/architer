package com.architer.challenge.infrastructure.persistence

import com.architer.challenge.domain.model.Challenge
import com.architer.challenge.domain.repository.ChallengeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
class ChallengeRepositoryAdapter(
    private val jpaRepository: JpaChallengeRepository
) : ChallengeRepository {
    override fun save(challenge: Challenge): Challenge = jpaRepository.save(challenge)
    override fun findAll(pageable: Pageable): Page<Challenge> = jpaRepository.findAllBy(pageable)
    override fun findById(id: UUID): Optional<Challenge> = jpaRepository.findById(id)
    override fun deleteById(id: UUID) = jpaRepository.deleteById(id)
}