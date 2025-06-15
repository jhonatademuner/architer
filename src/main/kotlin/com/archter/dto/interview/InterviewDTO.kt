package com.archter.dto.interview

import com.archter.dto.interview.message.InterviewMessageDTO
import java.time.LocalDateTime
import java.util.UUID

data class InterviewDTO(
    var id: UUID?,
    var title: String,
    var timeSpent: Int?, // in seconds
    var feedback: String?,
    var messages: MutableList<InterviewMessageDTO> = mutableListOf(),
    var assistantBehaviorId: UUID?,
    var challengeId: UUID?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?,
)