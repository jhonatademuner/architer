package com.architer.interview.infrastructure.persistence.message

import com.architer.interview.domain.model.InterviewMessage
import com.architer.interview.domain.model.enums.InterviewRole
import com.architer.interview.domain.repository.InterviewMessageRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class InterviewMessageRepositoryAdapter(
    private val jpaRepository: JpaInterviewMessageRepository
): InterviewMessageRepository {
    override fun save(entity: InterviewMessage): InterviewMessage = jpaRepository.save(entity)
    override fun findByInterviewId(interviewId: UUID): List<InterviewMessage> = jpaRepository.findAllByInterviewId(interviewId)
    override fun findById(id: UUID): java.util.Optional<InterviewMessage> = jpaRepository.findById(id)
    override fun deleteById(id: UUID) = jpaRepository.deleteById(id)
    override fun findAllByInterviewIdAndRoleNot(interviewId: UUID, role: InterviewRole): List<InterviewMessage> = jpaRepository.findAllByInterviewIdAndRoleNot(interviewId, role)
    override fun findAllByInterviewId(interviewId: UUID): List<InterviewMessage> = jpaRepository.findAllByInterviewId(interviewId)
}