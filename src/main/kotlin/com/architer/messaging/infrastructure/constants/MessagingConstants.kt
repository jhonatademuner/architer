package com.architer.messaging.infrastructure.constants

object MessagingConstants {
    const val INTERVIEW_TOPIC = "interview"
    
    object Queues {
        const val INTERVIEW_FEEDBACK_REQUESTED = "interview.feedback-requested.queue"
        const val INTERVIEW_STARTED = "interview.started.queue"
        const val INTERVIEW_COMPLETED = "interview.completed.queue"
        const val INTERVIEW_CANCELLED = "interview.cancelled.queue"
    }
    
    object RoutingKeys {
        const val INTERVIEW_FEEDBACK_REQUESTED = "interview.feedback-requested"
        const val INTERVIEW_STARTED = "interview.started"
        const val INTERVIEW_COMPLETED = "interview.completed"
        const val INTERVIEW_CANCELLED = "interview.cancelled"
    }
}