package com.architer.interview.domain.repository

import com.architer.interview.domain.model.Interview
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

interface InterviewRepository {
     fun save(interview: Interview): Interview
     fun findById(id: UUID): Optional<Interview>
     fun findAll(pageable: Pageable): Page<Interview>
     fun deleteById(id: UUID)
     fun findAllByUserId(pageable: Pageable, userId: UUID): Page<Interview>
     fun findByIdAndUserId(id: UUID, userId: UUID): Optional<Interview>
}