package com.architer.interview.infrastructure.rabbitmq

import com.architer.messaging.domain.model.InterviewEvent
import com.architer.messaging.domain.model.enums.InterviewEventType
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class InterviewEventConsumer {

    @RabbitListener(queues = ["interview.queue"])
    fun handleInterviewEvent(@Payload event: InterviewEvent) {
        when (event.type) {
            InterviewEventType.FEEDBACK_REQUESTED -> handleFeedbackRequested(event)
            InterviewEventType.INTERVIEW_STARTED -> handleInterviewStarted(event)
            InterviewEventType.INTERVIEW_COMPLETED -> handleInterviewCompleted(event)
            InterviewEventType.INTERVIEW_CANCELLED -> handleInterviewCancelled(event)
        }
    }

    private fun handleFeedbackRequested(event: InterviewEvent) {
        println("Processing feedback for interview: ${event.interviewId}")
        // Add your actual feedback generation logic
    }

    private fun handleInterviewStarted(event: InterviewEvent) {
        println("Interview started: ${event.interviewId}")
        // Add logic for interview started
    }

    private fun handleInterviewCompleted(event: InterviewEvent) {
        println("Interview completed: ${event.interviewId}")
        // Add logic for interview completed
    }

    private fun handleInterviewCancelled(event: InterviewEvent) {
        println("Interview cancelled: ${event.interviewId}")
        // Add logic for interview cancelled
    }
}