package com.architer.interview.infrastructure.persistence

import com.architer.interview.domain.model.Interview
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface JpaInterviewRepository : JpaRepository<Interview, UUID> {
    fun findAllBy(pageable: Pageable): Page<Interview>
    fun findAllByUserId(pageable: Pageable, userId: UUID): Page<Interview>
    fun findByIdAndUserId(id: UUID, userId: UUID): Optional<Interview>
}