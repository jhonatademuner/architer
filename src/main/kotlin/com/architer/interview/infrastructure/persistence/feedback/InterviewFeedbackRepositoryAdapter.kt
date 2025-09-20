package com.architer.interview.infrastructure.persistence.feedback

import com.architer.interview.domain.model.InterviewFeedback
import com.architer.interview.domain.repository.InterviewFeedbackRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class InterviewFeedbackRepositoryAdapter(
    private val jpaRepository: JpaInterviewFeedbackRepository
): InterviewFeedbackRepository {
    override fun save(feedback: InterviewFeedback): InterviewFeedback = jpaRepository.save(feedback)
    override fun findByInterviewId(interviewId: UUID): List<InterviewFeedback> = jpaRepository.findAllByInterviewId(interviewId)
    override fun findById(id: UUID): java.util.Optional<InterviewFeedback> = jpaRepository.findById(id)
    override fun deleteById(id: UUID) = jpaRepository.deleteById(id)
}