package com.architer.interview.infrastructure.rabbitmq.publisher

import com.architer.messaging.application.EventPublisher
import com.architer.messaging.domain.model.InterviewEvent
import com.architer.messaging.infrastructure.constants.MessagingConstants
import org.springframework.stereotype.Component

@Component
class InterviewEventPublisher(
    private val eventPublisher: EventPublisher
) {
    fun publishInterviewStartedEvent(event: InterviewEvent) {
        eventPublisher.publishEvent(MessagingConstants.RoutingKeys.INTERVIEW_STARTED, event)
    }

    fun publishInterviewCompletedEvent(event: InterviewEvent) {
        eventPublisher.publishEvent(MessagingConstants.RoutingKeys.INTERVIEW_COMPLETED, event)
    }

    fun publishInterviewCanceledEvent(event: InterviewEvent) {
        eventPublisher.publishEvent(MessagingConstants.RoutingKeys.INTERVIEW_CANCELLED, event)
    }

    fun publishInterviewFeedbackRequestEvent(event: InterviewEvent) {
        eventPublisher.publishEvent(MessagingConstants.RoutingKeys.INTERVIEW_FEEDBACK_REQUESTED, event)
    }
}