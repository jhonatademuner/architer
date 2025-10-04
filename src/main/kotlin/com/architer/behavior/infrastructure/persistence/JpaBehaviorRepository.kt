package com.architer.behavior.infrastructure.persistence

import com.architer.behavior.domain.model.Behavior
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface JpaBehaviorRepository : JpaRepository<Behavior, UUID> {
    fun findAllBy(pageable: Pageable): Page<Behavior>
    fun findAllByPublishedIsTrue(pageable: Pageable): Page<Behavior>
    fun findByIdAndPublishedIsTrue(id: UUID): Optional<Behavior>
    fun findByExternalId(externalId: String): Optional<Behavior>
}