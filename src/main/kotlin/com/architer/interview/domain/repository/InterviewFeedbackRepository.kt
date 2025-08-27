package com.architer.interview.domain.repository

import com.architer.interview.domain.model.InterviewFeedback
import java.util.Optional
import java.util.UUID

interface InterviewFeedbackRepository {
    fun save(feedback: InterviewFeedback): InterviewFeedback
    fun findByInterviewId(interviewId: UUID): List<InterviewFeedback>
    fun findById(id: UUID): Optional<InterviewFeedback>
    fun deleteById(id: UUID)
}