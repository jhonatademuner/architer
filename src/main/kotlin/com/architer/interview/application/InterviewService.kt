package com.architer.interview.application

import com.architer.ai.domain.model.message.AssistantBaseMessage
import com.architer.ai.infrastructure.AssistantPort
import com.architer.behavior.domain.repository.BehaviorRepository
import com.architer.challenge.domain.repository.ChallengeRepository
import com.architer.interview.domain.model.enums.InterviewRole
import com.architer.interview.domain.model.enums.InterviewSeniority
import com.architer.interview.domain.model.enums.InterviewStatus
import com.architer.interview.domain.repository.InterviewMessageRepository
import com.architer.interview.domain.repository.InterviewRepository
import com.architer.interview.infrastructure.rabbitmq.publisher.InterviewEventPublisher
import com.architer.interview.presentation.dto.InterviewCreateRequest
import com.architer.interview.presentation.dto.InterviewMessageResponse
import com.architer.interview.presentation.dto.InterviewResponse
import com.architer.interview.presentation.dto.InterviewSimplifiedResponse
import com.architer.interview.presentation.dto.InterviewMessageSendRequest
import com.architer.interview.presentation.mapper.InterviewMapper
import com.architer.interview.presentation.mapper.InterviewMessageMapper
import com.architer.messaging.domain.model.InterviewEvent
import com.architer.shared.application.CurrentUserHelper
import com.architer.shared.exception.InvalidStatusException
import com.architer.shared.exception.ResourceNotFoundException
import com.architer.shared.infrastructure.AppSettingsPort
import com.architer.shared.presentation.dto.PaginatedList
import com.architer.storage.infrastructure.StoragePort
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class InterviewService(
    private val interviewRepository: InterviewRepository,
    private val messageRepository: InterviewMessageRepository,
    private val behaviorRepository: BehaviorRepository,
    private val challengeRepository: ChallengeRepository,
    private val interviewMapper: InterviewMapper,
    private val messageMapper: InterviewMessageMapper,
    private val assistantPort: AssistantPort,
    private val storagePort: StoragePort,
    private val settingsPort: AppSettingsPort,
    private val eventPublisher: InterviewEventPublisher,
    private val currentUserHelper: CurrentUserHelper,
    private val messageUtils: MessageUtils
) {
    private final val INTERVIEW_CREATION_PROMPT_KEY = "INTERVIEW_CREATION_PROMPT"

    fun findAllWithFilters(
        searchTerm: String?,
        status: InterviewStatus?,
        seniority: InterviewSeniority?,
        page: Int,
        size: Int
    ): PaginatedList<InterviewSimplifiedResponse> {
        val pageable = PageRequest.of(page - 1, size)
        val pageResult = interviewRepository.findAllByUserIdWithFilters(
            userId = currentUserHelper.getCurrentUserId(),
            searchTerm = searchTerm,
            status = status,
            seniority = seniority,
            pageable = pageable
        ).map(interviewMapper::toSimplifiedResponse)
        return PaginatedList.from(pageResult)
    }

    fun findById(id: UUID): InterviewResponse {
        val entity = interviewRepository.findByIdAndUserId(id, currentUserHelper.getCurrentUserId())
            .orElseThrow { ResourceNotFoundException("Interview with id $id not found") }
        return interviewMapper.toResponse(entity)
    }

    fun findMessagesByInterviewId(interviewId: UUID): List<InterviewMessageResponse> {
        return messageRepository
            .findAllByInterviewIdAndRoleNot(interviewId, InterviewRole.system)
            .map(messageMapper::toResponse)
    }

    fun create(body: InterviewCreateRequest): InterviewSimplifiedResponse {
        val interviewCreationPrompt = settingsPort.findBySettingKey(INTERVIEW_CREATION_PROMPT_KEY).getValueAsString()
        val seniorityPrompt = "Consider that the candidate is at the ${body.seniority} level."
        val behavior = behaviorRepository.findById(body.behavior)
            .orElseThrow { ResourceNotFoundException("Behavior with id $body.behavior not found") }
        val challenge = challengeRepository.findById(body.challenge)
            .orElseThrow { ResourceNotFoundException("Challenge with id $body.challenge not found") }

        val messages : MutableList<AssistantBaseMessage> = messageUtils.assembleSystemMessages(
            interviewCreationPrompt,
            seniorityPrompt,
            behavior.toString(),
            challenge.toString()
        )

        messages.add(assistantPort.requestChatCompletion(messages).getMessage())
        val messagesEntities = messages.map(messageMapper::toEntity)

        val createdInterview = interviewMapper.toEntity(
            behavior = behavior,
            challenge = challenge,
            seniority = body.seniority,
            messages = messagesEntities,
            userId = currentUserHelper.getCurrentUserId()
        )
        return interviewMapper.toSimplifiedResponse(interviewRepository.save(createdInterview))
    }

    fun sendMessage(
        interviewId: UUID,
        messageRequest: InterviewMessageSendRequest,
        image: MultipartFile?
    ): InterviewMessageResponse {
        val imageUrl = getImageUrl(interviewId, image)
        val userMessage = messageUtils.assembleMessage(messageRequest, imageUrl)
        val messageList: List<AssistantBaseMessage> =
            messageRepository.findAllByInterviewId(interviewId)
                .map(messageMapper::toAssistantMessage)
                .plus(userMessage)

        val assistantResponse = assistantPort.requestChatCompletion(messageList)
        val interview = interviewRepository.findByIdAndUserId(interviewId, currentUserHelper.getCurrentUserId())
            .orElseThrow { ResourceNotFoundException("Interview with id $interviewId not found") }

        messageRepository.save(messageMapper.toEntity(userMessage, interview))
        val savedEntity = messageRepository.save(messageMapper.toEntity(assistantResponse.getMessage(), interview))
        return messageMapper.toResponse(savedEntity)
    }

    fun finish(interviewId: UUID) {
        val interview = interviewRepository.findByIdAndUserId(interviewId, currentUserHelper.getCurrentUserId())
            .orElseThrow { ResourceNotFoundException("Interview with id $interviewId not found") }
        if(interview.status != InterviewStatus.ON_GOING) throw InvalidStatusException("Interview with id $interviewId is not on going")

        eventPublisher.publishInterviewFeedbackRequestEvent(InterviewEvent(interviewId = interviewId))
        interview.status = InterviewStatus.WAITING_FOR_FEEDBACK
        interviewRepository.save(interview)
    }

    private fun getImageUrl(interviewId: UUID,image: MultipartFile?): String?{
        if (image != null && !image.isEmpty) return storagePort.uploadInterviewDiagram(interviewId, image)
        return null
    }
}