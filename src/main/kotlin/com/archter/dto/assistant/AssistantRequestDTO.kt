package com.archter.dto.assistant

import com.archter.dto.interview.message.InterviewMessageDTO
import lombok.Data

@Data
data class AssistantRequestDTO(
    var model: String,
    var messages: List<InterviewMessageDTO>,
)
