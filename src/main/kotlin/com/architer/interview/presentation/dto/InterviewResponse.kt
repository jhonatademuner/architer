package com.architer.interview.presentation.dto

import com.architer.behavior.presentation.dto.BehaviorResponse
import com.architer.challenge.presentation.dto.ChallengeResponse
import com.architer.interview.domain.model.enums.InterviewSeniority
import java.time.LocalDateTime
import java.util.UUID

data class InterviewResponse(
    var id: UUID? = null,
    var title: String,
    var duration: Int? = 0, // in seconds
    var feedback: InterviewFeedbackResponse? = null,
    var behavior: BehaviorResponse,
    var seniority: InterviewSeniority = InterviewSeniority.JUNIOR,
    var challenge: ChallengeResponse,
    var createdAt: LocalDateTime? = null,
)
