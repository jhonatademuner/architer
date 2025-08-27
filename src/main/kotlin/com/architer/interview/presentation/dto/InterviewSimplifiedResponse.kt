package com.architer.interview.presentation.dto

import java.time.LocalDateTime
import java.util.UUID

data class InterviewSimplifiedResponse(
    val id: UUID? = null,
    val title: String,
    val duration: Int? = 0, // in seconds
    val behaviorTitle: String,
    val challengeTitle: String,
    val seniority: String,
    val createdAt: LocalDateTime? = null
)
