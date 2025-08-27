package com.architer.ai.domain.model.message

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AssistantMessage(
    override val role: String,
    var content: String,
) : AssistantBaseMessage {
    override fun getTextMessage(): String {
        return content
    }
}