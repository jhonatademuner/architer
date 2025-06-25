package com.archter.dto.assistant

import com.archter.dto.interview.message.BaseMessageDTO

data class AssistantRequestDTO(
    var model: String,
    var messages: List<BaseMessageDTO>,
)
