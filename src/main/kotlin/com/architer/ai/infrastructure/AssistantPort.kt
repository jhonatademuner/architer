package com.architer.ai.infrastructure

import com.architer.ai.domain.model.message.AssistantBaseMessage
import com.architer.ai.domain.model.message.AssistantMessage

interface AssistantPort {
    fun requestChatCompletion(messages: List<AssistantBaseMessage>): AssistantMessage
}