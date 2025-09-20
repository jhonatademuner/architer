package com.architer.interview.presentation.dto

import com.architer.interview.domain.model.enums.InterviewDetailedFeedbackSubject
import com.architer.interview.domain.model.enums.InterviewFeedbackEvaluation
import java.util.UUID

data class InterviewDetailedFeedbackResponse(
    val id: UUID? = null,
    val subject: InterviewDetailedFeedbackSubject,
    val evaluation: InterviewFeedbackEvaluation,
    val remarks: String,
)
