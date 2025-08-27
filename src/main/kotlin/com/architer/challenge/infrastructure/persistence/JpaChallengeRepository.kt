package com.architer.challenge.infrastructure.persistence

import com.architer.challenge.domain.model.Challenge
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface JpaChallengeRepository : JpaRepository<Challenge, UUID> {
    fun findAllBy(pageable: Pageable): Page<Challenge>
}
