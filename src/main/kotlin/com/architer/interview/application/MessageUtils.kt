package com.architer.interview.application

import com.architer.ai.domain.model.message.AssistantBaseMessage
import com.architer.ai.domain.model.message.AssistantCompoundMessage
import com.architer.ai.domain.model.message.AssistantMessage
import com.architer.interview.domain.model.enums.InterviewRole
import com.architer.interview.presentation.dto.InterviewMessageSendRequest
import org.springframework.stereotype.Component

@Component
class MessageUtils {
    fun assembleSystemMessages(
        vararg messagesTexts: String
    ): MutableList<AssistantBaseMessage> {
        val messages: MutableList<AssistantBaseMessage> = mutableListOf()

        for (text in messagesTexts) {
            messages.add(AssistantMessage(
                role = InterviewRole.system.toString(),
                content = text
            ))
        }

        return messages
    }

    fun assembleMessage(
        request: InterviewMessageSendRequest,
        imageUrl: String?
    ): AssistantBaseMessage {
        return AssistantCompoundMessage(
            role = request.role.toString(),
            text = request.text,
            imageUrl = imageUrl
        )
    }
}