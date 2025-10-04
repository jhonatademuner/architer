package com.architer.interview.infrastructure.rabbitmq.listener

import com.architer.ai.infrastructure.AssistantPort
import com.architer.behavior.domain.repository.BehaviorRepository
import com.architer.interview.application.MessageUtils
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
import com.architer.shared.infrastructure.AppSettingsPort
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class InterviewFeedbackRequestedListener(
    private val interviewRepository: InterviewRepository,
    private val messageRepository: InterviewMessageRepository,
    private val messageMapper: InterviewMessageMapper,
    private val interviewFeedbackMapper: InterviewFeedbackMapper,
    private val assistantPort: AssistantPort,
    private val settingsPort: AppSettingsPort,
    private val messageUtils: MessageUtils
) {

    private val log = KotlinLogging.logger {}

    private final val INTERVIEWS_FEEDBACK_PROMPT_KEY = "INTERVIEWS_FEEDBACK_PROMPT"

    @RabbitListener(queues = [MessagingConstants.Queues.INTERVIEW_FEEDBACK_REQUESTED], containerFactory = "rabbitListenerContainerFactory")
    @Transactional
    fun handleFeedbackRequested(@Payload event: InterviewEvent) {
        log.info { "Processing feedback request for interview: ${event.interviewId}" }
        
        val interview = interviewRepository.findById(event.interviewId)
            .orElseThrow { ResourceNotFoundException("Interview with id ${event.interviewId} not found") }
        
        log.debug { "Found interview: ${interview.id} with behavior: ${interview.behavior.id} and challenge: ${interview.challenge.id}" }

        val interviewFeedbackPrompt = settingsPort.findBySettingKey(INTERVIEWS_FEEDBACK_PROMPT_KEY).getValueAsString()

        val seniorityPrompt = "Consider that the candidate is at the ${interview.seniority} level."

        val messages = messageUtils.assembleSystemMessages(
            interviewFeedbackPrompt,
            seniorityPrompt,
            interview.behavior.toString(),
            interview.challenge.toString()
        )

        val interviewMessages = messageRepository
            .findAllByInterviewIdAndRoleNot(event.interviewId, InterviewRole.system)
            .map(messageMapper::toAssistantMessage)

        messages.addAll(interviewMessages)

        val assistantResponse = assistantPort.requestChatCompletion(messages, InterviewFeedbackResponseFormat.getOpenAISchema())
        val interviewFeedback = interviewFeedbackMapper.toEntity(assistantResponse.getMessage(), interview)
        interview.feedback = interviewFeedback
        interview.status = InterviewStatus.FINISHED
        interviewRepository.save(interview)
    }
}