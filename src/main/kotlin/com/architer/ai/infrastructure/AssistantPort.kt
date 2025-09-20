package com.architer.ai.infrastructure

import com.architer.ai.domain.model.OpenAIJsonSchema
import com.architer.ai.domain.model.ChatCompletionResponse
import com.architer.ai.domain.model.message.AssistantBaseMessage

interface AssistantPort {
    fun requestChatCompletion(messages: List<AssistantBaseMessage>): ChatCompletionResponse
    fun requestChatCompletion(messages: List<AssistantBaseMessage>, responseFormat: OpenAIJsonSchema?): ChatCompletionResponse
}