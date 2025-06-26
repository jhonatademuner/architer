package com.architer.dto.assistant

import com.architer.dto.interview.message.BaseMessageDTO

data class AssistantRequestDTO(
    var model: String,
    var messages: List<BaseMessageDTO>,
)
