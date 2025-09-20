package com.architer.interview.infrastructure.rabbitmq.listener

import com.architer.ai.domain.model.message.AssistantMessage
import com.architer.ai.infrastructure.AssistantPort
import com.architer.interview.domain.model.enums.InterviewRole
import com.architer.interview.domain.model.enums.InterviewStatus
import com.architer.interview.domain.repository.InterviewMessageRepository
import com.architer.interview.domain.repository.InterviewRepository
import com.architer.interview.infrastructure.ai.InterviewFeedbackResponseFormat
import com.architer.interview.presentation.mapper.InterviewFeedbackMapper
import com.architer.interview.presentation.mapper.InterviewMessageMapper
import com.architer.messaging.domain.model.InterviewEvent
import com.architer.messaging.infrastructure.constants.MessagingConstants
import com.architer.shared.exception.ResourceNotFoundException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class InterviewFeedbackRequestedListener(
    private val interviewRepository: InterviewRepository,
    private val messageRepository: InterviewMessageRepository,
    private val messageMapper: InterviewMessageMapper,
    private val interviewFeedbackMapper: InterviewFeedbackMapper,
    private val assistantPort: AssistantPort,
) {

    private val log = KotlinLogging.logger {}

    @RabbitListener(queues = [MessagingConstants.Queues.INTERVIEW_FEEDBACK_REQUESTED], containerFactory = "rabbitListenerContainerFactory")
    @Transactional
    fun handleFeedbackRequested(@Payload event: InterviewEvent) {
        log.info { "Processing feedback request for interview: ${event.interviewId}" }
        
        val interview = interviewRepository.findById(event.interviewId)
            .orElseThrow { ResourceNotFoundException("Interview with id ${event.interviewId} not found") }
        
        log.debug { "Found interview: ${interview.id} with behavior: ${interview.behavior.id} and challenge: ${interview.challenge.id}" }

        val behaviorMessage = AssistantMessage(
            role = InterviewRole.system.toString(),
            content = interview.behavior.toString()
        )

        val challengeMessage = AssistantMessage(
            role = InterviewRole.system.toString(),
            content = interview.challenge.toString()
        )

        val seniorityLevelMessage = AssistantMessage(
            role = InterviewRole.system.toString(),
            content = "Consider that the candidate is at the ${interview.seniority} level."
        )

        val assistantMessages = mutableListOf(behaviorMessage, seniorityLevelMessage, challengeMessage)
        val interviewMessages = messageRepository
            .findAllByInterviewIdAndRoleNot(event.interviewId, InterviewRole.system)
            .map(messageMapper::toAssistantMessage)
        
        log.debug { "Found ${interviewMessages.size} interview messages to include in context" }
        assistantMessages.addAll(interviewMessages)
        
        log.info { "Requesting AI feedback with ${assistantMessages.size} messages" }
        val assistantResponse = assistantPort.requestChatCompletion(assistantMessages, InterviewFeedbackResponseFormat.getOpenAISchema())
        log.info { "AI Response: ${assistantResponse.getMessage()}" }
        val interviewFeedback = interviewFeedbackMapper.toEntity(assistantResponse.getMessage(), interview)
        interview.feedback = interviewFeedback
        interview.status = InterviewStatus.COMPLETED
        interviewRepository.save(interview)
        log.info { "Received AI feedback response for interview: ${event.interviewId}" }
        log.debug { "AI Response: $assistantResponse" }
    }
}