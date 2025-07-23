package com.architer.dto.interview

import java.time.LocalDateTime
import java.util.UUID

data class SimplifiedInterviewDTO (
    var id: UUID? = null,
    var title: String,
    var timeSpent: Int? = null,
    var score: Int? = null,
    var behaviorTitle: String,
    var challengeTitle: String,
    var seniority: String,
    var createdAt: LocalDateTime? = null
)