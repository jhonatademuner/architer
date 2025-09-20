package com.architer.interview.infrastructure.rabbitmq.listener

import com.architer.messaging.domain.model.InterviewEvent
import com.architer.messaging.infrastructure.constants.MessagingConstants
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class InterviewCompletedListener {

    @RabbitListener(queues = [MessagingConstants.Queues.INTERVIEW_COMPLETED], containerFactory = "rabbitListenerContainerFactory")
    fun handleInterviewCompleted(@Payload event: InterviewEvent) {
        println("Interview completed: ${event.interviewId}")
        // Add logic for interview completed
    }
}