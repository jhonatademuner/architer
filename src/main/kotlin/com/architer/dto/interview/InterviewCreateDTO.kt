package com.architer.dto.interview

import com.architer.domain.interview.InterviewSeniorityLevel
import java.util.UUID

data class InterviewCreateDTO (
    val challengeId: UUID,
    val behaviorId: UUID,
    val seniorityLevel: InterviewSeniorityLevel,
)