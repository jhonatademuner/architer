package com.architer.interview.application

import com.architer.ai.application.AssistantService
import com.architer.ai.domain.model.message.AssistantBaseMessage
import com.architer.ai.domain.model.message.AssistantCompoundMessage
import com.architer.ai.domain.model.message.AssistantMessage
import com.architer.behavior.domain.repository.BehaviorRepository
import com.architer.challenge.domain.repository.ChallengeRepository
import com.architer.interview.domain.model.enums.InterviewRole
import com.architer.interview.domain.repository.InterviewMessageRepository
import com.architer.interview.domain.repository.InterviewRepository
import com.architer.interview.presentation.dto.InterviewCreateRequest
import com.architer.interview.presentation.dto.InterviewMessageResponse
import com.architer.interview.presentation.dto.InterviewResponse
import com.architer.interview.presentation.dto.InterviewSimplifiedResponse
import com.architer.interview.presentation.dto.InterviewMessageSendRequest
import com.architer.interview.presentation.mapper.InterviewMapper
import com.architer.interview.presentation.mapper.InterviewMessageMapper
import com.architer.shared.application.CurrentUserHelper
import com.architer.shared.exception.ResourceNotFoundException
import com.architer.shared.model.PaginatedList
import com.architer.storage.application.StorageService
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
    private val assistantService: AssistantService,
    private val storageService: StorageService,
    private val currentUserHelper: CurrentUserHelper,
) {

    fun findAll(page: Int, size: Int): PaginatedList<InterviewSimplifiedResponse> {
        val pageResult = repository
            .findAllByUserId(PageRequest.of(page-1, size), currentUserHelper.getCurrentUserId())
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
        val assistantResponse = assistantService.requestChatCompletion(assistantMessages)
        assistantMessages.add(assistantResponse)

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

        val assistantResponse = assistantService.requestChatCompletion(messageList)

        val interview = repository.findByIdAndUserId(interviewId, currentUserHelper.getCurrentUserId())
            .orElseThrow { ResourceNotFoundException("Interview with id $interviewId not found") }

        messageRepository.save(messageMapper.toEntity(userMessage, interview))
        val savedEntity = messageRepository.save(messageMapper.toEntity(assistantResponse, interview))

        return messageMapper.toResponse(savedEntity)
    }

    private fun assembleUserMessage(interviewId: UUID, userMessage: InterviewMessageSendRequest, image: MultipartFile?): AssistantBaseMessage {
        if(image != null && !image.isEmpty) {
            val imageUrl = storageService.uploadInterviewDiagram(interviewId, image)
            return AssistantCompoundMessage(
                role = userMessage.role.toString(),
                text = userMessage.text,
                imageUrl = imageUrl
            )
        }
        return AssistantMessage(role = InterviewRole.user.toString(), content = userMessage.text)
    }

}