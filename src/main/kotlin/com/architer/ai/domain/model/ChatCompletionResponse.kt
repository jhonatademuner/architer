package com.architer.ai.domain.model

import com.architer.ai.domain.model.message.AssistantMessage
import com.architer.shared.exception.ChatCompletionException
import com.fasterxml.jackson.annotation.JsonProperty

data class ChatCompletionResponse(
    var id: String,
    var model: String,
    var choices: List<Choice>,
    var usage: Usage,

    @JsonProperty("object")
    var objectType: String,

    @JsonProperty("created")
    var createdAt: Long,

    @JsonProperty("service_tier")
    var serviceTier: String
) {
    data class Choice(
        var index: Long,
        var message: AssistantChatResponseMessage,

        @JsonProperty("finish_reason")
        var finishReason: String
    )

    data class AssistantChatResponseMessage(
        var role: String,
        var content: String,
    )

    data class Usage(
        @JsonProperty("prompt_tokens")
        val promptTokens: Long,

        @JsonProperty("completion_tokens")
        val completionTokens: Long,

        @JsonProperty("total_tokens")
        val totalTokens: Long,

        @JsonProperty("prompt_tokens_details")
        val promptTokensDetails: PromptTokensDetails,

        @JsonProperty("completion_tokens_details")
        val completionTokensDetails: CompletionTokensDetails
    )

    data class PromptTokensDetails(
        @JsonProperty("cached_tokens")
        val cachedTokens: Long,

        @JsonProperty("audio_tokens")
        val audioTokens: Long
    )

    data class CompletionTokensDetails(
        @JsonProperty("reasoning_tokens")
        val reasoningTokens: Long,

        @JsonProperty("audio_tokens")
        val audioTokens: Long,

        @JsonProperty("accepted_prediction_tokens")
        val acceptedPredictionTokens: Long,

        @JsonProperty("rejected_prediction_tokens")
        val rejectedPredictionTokens: Long
    )

    fun getMessage(): AssistantMessage {
        val message: AssistantChatResponseMessage = choices.firstOrNull()?.message
            ?: throw ChatCompletionException("No message found in the assistant response.")
        return AssistantMessage(
            role = message.role,
            content = message.content
        )
    }

}
