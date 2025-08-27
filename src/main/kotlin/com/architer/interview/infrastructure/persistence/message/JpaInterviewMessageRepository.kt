package com.architer.interview.infrastructure.persistence.message

import com.architer.interview.domain.model.InterviewMessage
import com.architer.interview.domain.model.enums.InterviewRole
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface JpaInterviewMessageRepository : JpaRepository<InterviewMessage, UUID> {
    fun findAllByInterviewId(interviewId: UUID): List<InterviewMessage>
    fun findAllByInterviewIdAndRoleNot(interviewId: UUID, role: InterviewRole): List<InterviewMessage>
}