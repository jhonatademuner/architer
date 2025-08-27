package com.architer.interview.presentation.dto

import com.architer.interview.domain.model.enums.InterviewRole
import java.time.LocalDateTime
import java.util.UUID

data class InterviewMessageResponse(
    val id: UUID? = null,
    val role: InterviewRole,
    var content: String,
    var sentAt: LocalDateTime?
)
