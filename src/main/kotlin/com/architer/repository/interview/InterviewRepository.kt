package com.architer.repository.interview

import com.architer.domain.interview.Interview
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface InterviewRepository  : JpaRepository<Interview, UUID> {
    fun findAllByUserId(pageable: Pageable, userId: UUID): Page<Interview>
    fun findByIdAndUserId(id: UUID, userId: UUID): Optional<Interview>
}