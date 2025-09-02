package com.architer.messaging.domain.model

import com.architer.messaging.domain.model.enums.InterviewEventType
import java.util.UUID

data class InterviewEvent(
    override val eventId: UUID = UUID.randomUUID(),
    val type: InterviewEventType,
    val interviewId: UUID,
    override val timestamp: Long = System.currentTimeMillis(),
    val metadata: Map<String, Any> = emptyMap()
) : Event