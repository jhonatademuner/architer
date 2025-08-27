package com.architer.interview.infrastructure.persistence.feedback

import com.architer.interview.domain.model.InterviewFeedback
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface JpaInterviewFeedbackRepository : JpaRepository<InterviewFeedback, UUID> {
}