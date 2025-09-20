package com.architer.ai.application

import com.architer.ai.domain.model.OpenAIJsonSchema
import com.architer.ai.domain.model.ChatCompletionRequest
import com.architer.ai.domain.model.ChatCompletionResponse
import com.architer.ai.domain.model.message.AssistantBaseMessage
import com.architer.ai.infrastructure.client.OpenAiClient
import com.architer.ai.infrastructure.AssistantPort
import com.architer.shared.exception.ChatCompletionException
import org.springframework.stereotype.Service

@Service
class OpenAIService(
    private val assistantClient: OpenAiClient
): AssistantPort {

    override fun requestChatCompletion(messages: List<AssistantBaseMessage>): ChatCompletionResponse {
        return requestChatCompletion(messages, null)
    }

    override fun requestChatCompletion(messages: List<AssistantBaseMessage>, responseFormat: OpenAIJsonSchema?): ChatCompletionResponse {
        val requestBody = ChatCompletionRequest(
            model = "o4-mini",
            messages = messages,
        )
        requestBody.setResponseFormat(responseFormat)

        val response = assistantClient.post(
            endpoint = "v1/chat/completions",
            request = requestBody,
            responseType = ChatCompletionResponse::class.java
        )

        val rawResponse = response.body ?: throw ChatCompletionException("Failed to get assistant message")
        return rawResponse
    }
}