package com.architer.interview.infrastructure.rabbitmq.listener

import com.architer.messaging.domain.model.InterviewEvent
import com.architer.messaging.infrastructure.constants.MessagingConstants
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class InterviewCancelledListener {

    @RabbitListener(queues = [MessagingConstants.Queues.INTERVIEW_CANCELLED], containerFactory = "rabbitListenerContainerFactory")
    fun handleInterviewCancelled(@Payload event: InterviewEvent) {
        println("Interview cancelled: ${event.interviewId}")
        // Add logic for interview cancelled
    }
}