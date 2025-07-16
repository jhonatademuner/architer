package com.architer.dto.interview

import com.architer.dto.assistant.behavior.AssistantBehaviorDTO
import com.architer.dto.challenge.ChallengeDTO
import com.architer.dto.interview.message.InterviewMessageDTO
import java.time.LocalDateTime
import java.util.UUID

data class InterviewDTO(
    var id: UUID? = null,
    var title: String,
    var timeSpent: Int? = null, // in seconds
    var feedback: String? = null,
    var messages: MutableList<InterviewMessageDTO> = mutableListOf(),
    var assistantBehavior: AssistantBehaviorDTO?,
    var challenge: ChallengeDTO? = null,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
)