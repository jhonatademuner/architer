package com.architer.interview.infrastructure.persistence

import com.architer.interview.domain.model.Interview
import com.architer.interview.domain.model.enums.InterviewSeniority
import com.architer.interview.domain.model.enums.InterviewStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional
import java.util.UUID

interface JpaInterviewRepository : JpaRepository<Interview, UUID> {
    fun findAllBy(pageable: Pageable): Page<Interview>
    fun findAllByUserId(pageable: Pageable, userId: UUID): Page<Interview>
    fun findByIdAndUserId(id: UUID, userId: UUID): Optional<Interview>
    
    @Query("""
        SELECT i FROM Interview i 
        WHERE i.userId = :userId 
        AND (:searchTerm IS NULL OR i.behavior.title LIKE %:searchTerm% OR i.challenge.title LIKE %:searchTerm%)
        AND (:status IS NULL OR i.status = :status)
        AND (:seniority IS NULL OR i.seniority = :seniority)
    """)
    fun findAllByUserIdWithFilters(
        @Param("userId") userId: UUID,
        @Param("searchTerm") searchTerm: String?,
        @Param("status") status: InterviewStatus?,
        @Param("seniority") seniority: InterviewSeniority?,
        pageable: Pageable
    ): Page<Interview>
}