package com.architer.interview.domain.repository

import com.architer.interview.domain.model.InterviewFeedback
import com.architer.interview.domain.model.InterviewMessage
import com.architer.interview.domain.model.enums.InterviewRole
import java.util.Optional
import java.util.UUID

interface InterviewMessageRepository {
    fun save(entity: InterviewMessage): InterviewMessage
    fun findByInterviewId(interviewId: UUID): List<InterviewMessage>
    fun findById(id: UUID): Optional<InterviewMessage>
    fun deleteById(id: UUID)
    fun findAllByInterviewIdAndRoleNot(interviewId: UUID, role: InterviewRole): List<InterviewMessage>
    fun findAllByInterviewId(interviewId: UUID): List<InterviewMessage>
}