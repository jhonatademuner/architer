package com.architer.ai.domain.model

import com.architer.ai.domain.model.message.AssistantBaseMessage

data class ChatCompletionRequest(
    var model: String,
    var messages: List<AssistantBaseMessage>,
)
