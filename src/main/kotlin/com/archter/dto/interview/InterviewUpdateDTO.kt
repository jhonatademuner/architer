package com.archter.dto.interview

import java.util.UUID

data class InterviewUpdateDTO(
    val id: UUID,
    val title: String,
    val timeSpent: Int,
    val feedback: String,
)
