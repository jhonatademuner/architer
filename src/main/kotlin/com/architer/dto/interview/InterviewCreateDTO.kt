package com.architer.dto.interview

import com.architer.domain.interview.InterviewSeniorityLevel
import java.util.UUID

data class InterviewCreateDTO (
    val challenge: UUID,
    val behavior: UUID,
    val seniority: InterviewSeniorityLevel,
)