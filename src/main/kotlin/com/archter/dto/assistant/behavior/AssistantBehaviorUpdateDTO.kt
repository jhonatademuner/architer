package com.archter.dto.assistant.behavior

import java.util.UUID

data class AssistantBehaviorUpdateDTO(
    val id: UUID,
    val title: String,
    val overview: String,
    val content: String,
)