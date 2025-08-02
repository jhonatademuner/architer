package com.architer.repository.interview

import com.architer.domain.interview.InterviewRole
import com.architer.domain.interview.message.InterviewMessage
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface InterviewMessageRepository : JpaRepository<InterviewMessage, UUID> {
    fun findAllByInterviewIdAndRoleNot(interviewId: UUID, role: InterviewRole): List<InterviewMessage>
    fun findAllByInterviewId(interviewId: UUID): List<InterviewMessage>
}