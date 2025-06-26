package com.architer.dto.interview

import com.architer.dto.interview.message.InterviewMessageDTO
import java.util.UUID

data class InterviewCreateDTO (
    val title: String,
    val messages: MutableList<InterviewMessageDTO> = mutableListOf(),
    val assistantBehaviorId: UUID?,
    val challengeId: UUID?
)