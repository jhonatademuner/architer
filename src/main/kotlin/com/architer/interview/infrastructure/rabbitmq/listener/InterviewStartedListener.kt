package com.architer.interview.infrastructure.rabbitmq.listener

import com.architer.messaging.domain.model.InterviewEvent
import com.architer.messaging.infrastructure.constants.MessagingConstants
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class InterviewStartedListener {

    @RabbitListener(queues = [MessagingConstants.Queues.INTERVIEW_STARTED], containerFactory = "rabbitListenerContainerFactory")
    fun handleInterviewStarted(@Payload event: InterviewEvent) {
        println("Interview started: ${event.interviewId}")
        // Add logic for interview started
    }
}