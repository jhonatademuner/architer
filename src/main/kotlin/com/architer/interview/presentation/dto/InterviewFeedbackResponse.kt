package com.architer.interview.presentation.dto

import com.architer.interview.domain.model.enums.InterviewFeedbackEvaluation
import java.util.UUID

data class InterviewFeedbackResponse(
    val id: UUID? = null,
    val overallEvaluation: InterviewFeedbackEvaluation,
    val overallRemarks: String,
    val detailedFeedbacks: List<InterviewDetailedFeedbackResponse> = emptyList(),
)
