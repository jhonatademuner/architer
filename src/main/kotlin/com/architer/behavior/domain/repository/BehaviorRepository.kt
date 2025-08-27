package com.architer.behavior.domain.repository

import com.architer.behavior.domain.model.Behavior
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

interface BehaviorRepository {
    fun save(behavior: Behavior): Behavior
    fun findAll(pageable: Pageable): Page<Behavior>
    fun findById(id: UUID): Optional<Behavior>
    fun deleteById(id: UUID): Unit
}