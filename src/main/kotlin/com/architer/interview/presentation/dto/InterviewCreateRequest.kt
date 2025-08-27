package com.architer.interview.presentation.dto

import com.architer.interview.domain.model.enums.InterviewSeniority
import java.util.UUID

data class InterviewCreateRequest (
    val challenge: UUID,
    val behavior: UUID,
    val seniority: InterviewSeniority,
)