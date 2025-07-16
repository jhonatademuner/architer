package com.architer.dto.interview

import com.architer.domain.interview.InterviewSeniorityLevel
import java.util.UUID

data class InterviewCreateDTO (
    val challengeId: UUID,
    val assistantBehaviorId: UUID,
    val seniorityLevel: InterviewSeniorityLevel,
)