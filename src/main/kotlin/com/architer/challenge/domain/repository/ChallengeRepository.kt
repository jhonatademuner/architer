package com.architer.challenge.domain.repository

import com.architer.challenge.domain.model.Challenge
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

interface ChallengeRepository {
    fun save(challenge: Challenge): Challenge
    fun findAll(pageable: Pageable): Page<Challenge>
    fun findById(id: UUID): Optional<Challenge>
    fun deleteById(id: UUID)
}