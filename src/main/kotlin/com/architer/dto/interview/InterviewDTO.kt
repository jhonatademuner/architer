package com.architer.dto.interview

import com.architer.domain.interview.InterviewSeniorityLevel
import com.architer.dto.behavior.BehaviorDTO
import com.architer.dto.challenge.ChallengeDTO
import java.time.LocalDateTime
import java.util.UUID

data class InterviewDTO(
    var id: UUID? = null,
    var title: String,
    var timeSpent: Int? = null, // in seconds
    var feedback: String? = null,
    var score: Int? = null, // 0-100
    var behavior: BehaviorDTO?,
    var seniority: InterviewSeniorityLevel = InterviewSeniorityLevel.JUNIOR,
    var challenge: ChallengeDTO? = null,
    var user: UUID? = null,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
)