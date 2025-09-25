package com.architer.interview.application

import com.architer.ai.domain.model.message.AssistantBaseMessage
import com.architer.ai.domain.model.message.AssistantCompoundMessage
import com.architer.ai.domain.model.message.AssistantMessage
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
import com.architer.shared.exception.ResourceNotFoundException
import com.architer.shared.model.PaginatedList
import com.architer.storage.infrastructure.StoragePort
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class InterviewService(
    private val repository: InterviewRepository,
    private val messageRepository: InterviewMessageRepository,
    private val behaviorRepository: BehaviorRepository,
    private val challengeRepository: ChallengeRepository,
    private val mapper: InterviewMapper,
    private val messageMapper: InterviewMessageMapper,
    private val assistantPort: AssistantPort,
    private val storagePort: StoragePort,
    private val eventPublisher: InterviewEventPublisher,
    private val currentUserHelper: CurrentUserHelper,
) {

    fun findAll(
        term: String?,
        status: InterviewStatus?,
        seniority: InterviewSeniority?,
        page: Int,
        size: Int
    ): PaginatedList<InterviewSimplifiedResponse> {
        val pageable = PageRequest.of(page-1, size)
        
        val pageResult = repository
            .findAllByUserIdWithFilters(
                currentUserHelper.getCurrentUserId(),
                term,
                status,
                seniority,
                pageable
            )
            .map(mapper::toSimplifiedResponse)
        return PaginatedList.from(pageResult)
    }

    fun findById(id: UUID): InterviewResponse {
        val entity = repository.findByIdAndUserId(id, currentUserHelper.getCurrentUserId())
            .orElseThrow { ResourceNotFoundException("Interview with id $id not found") }
        return mapper.toResponse(entity)
    }

    fun findMessagesByInterviewId(interviewId: UUID): List<InterviewMessageResponse> {
        return messageRepository
            .findAllByInterviewIdAndRoleNot(interviewId, InterviewRole.system)
            .map(messageMapper::toResponse)
    }

    fun create(body: InterviewCreateRequest): InterviewSimplifiedResponse {
        val behavior = behaviorRepository.findById(body.behavior)
            .orElseThrow { ResourceNotFoundException("Behavior with id $body.behavior not found") }

        val challenge = challengeRepository.findById(body.challenge)
            .orElseThrow { ResourceNotFoundException("Challenge with id $body.challenge not found") }

        val behaviorMessage = AssistantMessage(
            role = InterviewRole.system.toString(),
            content = behavior.toString()
        )

        val challengeMessage = AssistantMessage(
            role = InterviewRole.system.toString(),
            content = challenge.toString()
        )

        val seniorityLevelMessage = AssistantMessage(
            role = InterviewRole.system.toString(),
            content = "Consider that the candidate is at the ${body.seniority} level."
        )

        val assistantMessages = mutableListOf(behaviorMessage, seniorityLevelMessage, challengeMessage)
        val assistantResponse = assistantPort.requestChatCompletion(assistantMessages)
        assistantMessages.add(assistantResponse.getMessage())

        val messages = assistantMessages
            .map(messageMapper::toEntity)

        val interview = mapper.toEntity(
            behavior = behavior,
            challenge = challenge,
            seniority = body.seniority,
            messages = messages,
            userId = currentUserHelper.getCurrentUserId()
        )

        return mapper.toSimplifiedResponse(repository.save(interview))
    }

    fun sendMessage(interviewId: UUID, messageRequest: InterviewMessageSendRequest, image: MultipartFile?): InterviewMessageResponse {
        val userMessage = assembleUserMessage(interviewId, messageRequest, image)

        val messageList: List<AssistantBaseMessage> =
            messageRepository.findAllByInterviewId(interviewId)
                .map(messageMapper::toAssistantMessage)
                .plus(userMessage)

        val assistantResponse = assistantPort.requestChatCompletion(messageList)

        val interview = repository.findByIdAndUserId(interviewId, currentUserHelper.getCurrentUserId())
            .orElseThrow { ResourceNotFoundException("Interview with id $interviewId not found") }

        messageRepository.save(messageMapper.toEntity(userMessage, interview))
        val savedEntity = messageRepository.save(messageMapper.toEntity(assistantResponse.getMessage(), interview))

        return messageMapper.toResponse(savedEntity)
    }

    private fun assembleUserMessage(interviewId: UUID, userMessage: InterviewMessageSendRequest, image: MultipartFile?): AssistantBaseMessage {
        if(image != null && !image.isEmpty) {
            val imageUrl = storagePort.uploadInterviewDiagram(interviewId, image)
            return AssistantCompoundMessage(
                role = userMessage.role.toString(),
                text = userMessage.text,
                imageUrl = imageUrl
            )
        }
        return AssistantMessage(role = InterviewRole.user.toString(), content = userMessage.text)
    }

    fun finish(interviewId: UUID) {
        val interview = repository.findByIdAndUserId(interviewId, currentUserHelper.getCurrentUserId())
            .orElseThrow { ResourceNotFoundException("Interview with id $interviewId not found") }
        interview.status = InterviewStatus.WAITING_FOR_FEEDBACK
        val event = InterviewEvent(
            interviewId = interviewId
        )
        eventPublisher.publishInterviewFeedbackRequestEvent(event)
        repository.save(interview)
    }

}