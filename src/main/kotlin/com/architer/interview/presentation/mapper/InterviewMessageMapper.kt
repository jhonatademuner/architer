package com.architer.interview.presentation.mapper

import com.architer.ai.domain.model.message.AssistantBaseMessage
import com.architer.ai.domain.model.message.AssistantMessage
import com.architer.interview.domain.model.Interview
import com.architer.interview.domain.model.InterviewMessage
import com.architer.interview.domain.model.enums.InterviewRole
import com.architer.interview.presentation.dto.InterviewMessageResponse
import org.springframework.stereotype.Component

@Component
class InterviewMessageMapper {
    fun toResponse(entity: InterviewMessage): InterviewMessageResponse {
        return InterviewMessageResponse(
            id = entity.id,
            role = entity.role,
            content = entity.content,
            sentAt = entity.sentAt
        )
    }

    fun toAssistantMessage(entity: InterviewMessage): AssistantMessage {
        return AssistantMessage(
            role = entity.role.toString(),
            content = entity.content
        )
    }

    fun toEntity(assistantMessage: AssistantBaseMessage): InterviewMessage {
        return InterviewMessage(
            role = InterviewRole.valueOf(assistantMessage.role),
            content = assistantMessage.getTextMessage()
        )
    }

    fun toEntity(assistantMessage: AssistantBaseMessage, interview: Interview): InterviewMessage {
        return InterviewMessage(
            interview = interview,
            role = InterviewRole.valueOf(assistantMessage.role),
            content = assistantMessage.getTextMessage()
        )
    }
}