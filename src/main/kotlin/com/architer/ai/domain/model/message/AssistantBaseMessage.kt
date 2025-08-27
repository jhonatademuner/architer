package com.architer.ai.domain.model.message

interface AssistantBaseMessage {
    val role: String
    fun getTextMessage(): String
}