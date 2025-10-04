package com.architer.interview.infrastructure.persistence

import com.architer.interview.domain.model.Interview
import com.architer.interview.domain.model.enums.InterviewSeniority
import com.architer.interview.domain.model.enums.InterviewStatus
import com.architer.interview.domain.repository.InterviewRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
class InterviewRepositoryAdapter(
    private val jpaRepository: JpaInterviewRepository
): InterviewRepository {
    override fun save(interview: Interview): Interview = jpaRepository.save(interview)
    override fun findAll(pageable: Pageable): Page<Interview> = jpaRepository.findAllBy(pageable)
    override fun findById(id: UUID): Optional<Interview> = jpaRepository.findById(id)
    override fun deleteById(id: UUID) = jpaRepository.deleteById(id)
    override fun findAllByUserId(pageable: Pageable, userId: UUID): Page<Interview> = jpaRepository.findAllByUserId(pageable, userId)
    override fun findByIdAndUserId(id: UUID, userId: UUID): Optional<Interview> = jpaRepository.findByIdAndUserId(id, userId)
    override fun findAllByUserIdWithFilters(
        userId: UUID,
        searchTerm: String?,
        status: InterviewStatus?,
        seniority: InterviewSeniority?,
        pageable: Pageable
    ): Page<Interview> = jpaRepository.findAllByUserIdWithFilters(userId, searchTerm, status, seniority, pageable)
}