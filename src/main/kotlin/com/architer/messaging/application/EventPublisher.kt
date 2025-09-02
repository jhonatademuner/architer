package com.architer.messaging.application

import com.architer.messaging.domain.model.Event
import com.architer.messaging.domain.model.InterviewEvent
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class EventPublisher(
    private val rabbitTemplate: RabbitTemplate
) {
    // Convenience methods for specific event types
    fun publishInterviewEvent(event: InterviewEvent) {
        publishEvent(event, "interview.${event.type.name.lowercase()}")
    }

    fun publishEvent(event: Event, routingKey: String) {
        rabbitTemplate.convertAndSend("architer.events", routingKey, event)
    }

}