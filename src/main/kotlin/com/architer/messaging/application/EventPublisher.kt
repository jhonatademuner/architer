package com.architer.messaging.application

import com.architer.messaging.domain.model.Event
import com.architer.messaging.domain.model.InterviewEvent
import com.architer.messaging.infrastructure.config.RabbitMQProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class EventPublisher(
    private val rabbitTemplate: RabbitTemplate,
    private val rabbitMQProperties: RabbitMQProperties,
) {
    fun publishEvent(routingKey: String, event: Event) {
        rabbitTemplate.convertAndSend(rabbitMQProperties.exchange, routingKey, event)
    }
}