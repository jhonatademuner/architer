package com.architer.service.interview

import com.architer.assembler.behavior.BehaviorAssembler
import com.architer.assembler.challenge.ChallengeAssembler
import com.architer.assembler.interview.InterviewAssembler
import com.architer.assembler.interview.InterviewMessageAssembler
import com.architer.client.assistant.AssistantClient
import com.architer.domain.interview.InterviewRole
import com.architer.dto.assistant.AssistantRequestDTO
import com.architer.dto.assistant.AssistantResponseDTO
import com.architer.dto.interview.InterviewCreateDTO
import com.architer.dto.interview.InterviewDTO
import com.architer.dto.interview.SimplifiedInterviewDTO
import com.architer.dto.interview.message.BaseMessageDTO
import com.architer.dto.interview.message.InterviewCompoundMessageDTO
import com.architer.dto.interview.message.InterviewMessageCreateDTO
import com.architer.dto.interview.message.InterviewMessageDTO
import com.architer.repository.behavior.BehaviorRepository
import com.architer.repository.challenge.ChallengeRepository
import com.architer.repository.interview.InterviewMessageRepository
import com.architer.repository.interview.InterviewRepository
import com.architer.service.minio.MinioService
import com.architer.service.user.UserService
import com.architer.utils.exception.AssistantChatException
import com.architer.utils.exception.ResourceNotFoundException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Service
class InterviewService(
    private val assistantClient: AssistantClient,
    private val minioService: MinioService,
    private val interviewRepository: InterviewRepository,
    private val behaviorRepository: BehaviorRepository,
    private val challengeRepository: ChallengeRepository,
    private val interviewMessageRepository: InterviewMessageRepository,
    private val interviewAssembler: InterviewAssembler,
    private val challengeAssembler: ChallengeAssembler,
    private val behaviorAssembler: BehaviorAssembler,
    private val messageAssembler: InterviewMessageAssembler,
    private val userService: UserService
) {

    fun findAll(page: Int, size: Int): List<SimplifiedInterviewDTO> {
        logger.info { "Finding all interviews - page: $page, size: $size" }
        val entityList = interviewRepository.findAllByUserId(PageRequest.of(page, size), userService.getCurrentUserId()).content
        return interviewAssembler.toSimplifiedDtoList(entityList)
    }

    fun findById(id: UUID): InterviewDTO {
        logger.info { "Finding chat by ID: $id" }
        val entity = interviewRepository.findByIdAndUserId(id, userService.getCurrentUserId())
            .orElseThrow { ResourceNotFoundException("Interview with id $id not found") }
        return interviewAssembler.toDto(entity)
    }

    fun findMessagesByInterviewId(interviewId: UUID): MutableList<InterviewMessageDTO> {
        logger.info { "Finding messages for interview ID: $interviewId" }
        val messages = interviewMessageRepository.findAllByInterviewIdAndRoleNot(interviewId, InterviewRole.system).toMutableList()
        return messageAssembler.toDtoList(messages)
    }

    fun create(body: InterviewCreateDTO): SimplifiedInterviewDTO {
        val challengeId = body.challenge
        val behaviorId = body.behavior

        logger.info { "Creating chat - challengeId: $challengeId, assistantBehaviorId: $behaviorId" }

        val behavior = behaviorRepository.findById(behaviorId)
            .orElseThrow { ResourceNotFoundException("AssistantBehavior with id $behaviorId not found") }

        val challenge = challengeRepository.findById(challengeId)
            .orElseThrow { ResourceNotFoundException("Challenge with id $challengeId not found") }

        val behaviorMessage = InterviewMessageDTO(
            role = InterviewRole.system,
            content = behavior.toString()
        )

        val challengeMessage = InterviewMessageDTO(
            role = InterviewRole.system,
            content = challenge.toString()
        )

        val seniorityLevelMessage = InterviewMessageDTO(
            role = InterviewRole.system,
            content = "Consider that the candidate is at the ${body.seniority} level."
        )

        val messages = mutableListOf(behaviorMessage, seniorityLevelMessage, challengeMessage)

        val assistantResponse = requestChatCompletion(messages)
        messages.add(assistantResponse)

        val interviewDto = InterviewDTO(
            title = challenge.title,
            behavior = behaviorAssembler.toDto(behavior),
            challenge = challengeAssembler.toDto(challenge)
        )

        val interview = interviewAssembler.toEntity(interviewDto, messages)

        return interviewAssembler.toSimplifiedDto(interviewRepository.save(interview))
    }

    fun sendMessage(interviewId: UUID, message: InterviewMessageCreateDTO, image: MultipartFile?): InterviewMessageDTO {
        logger.info { "Sending message to chat - interviewId: $interviewId" }

        logger.info { "Message content: $message" }
        logger.info { "Image: ${image?.originalFilename ?: "No image provided"}" }

        val imageUrl = if (image != null && !image.isEmpty) {
            minioService.uploadInterviewSystemDesign(interviewId, image)
        } else null

        val compoundMessage = InterviewCompoundMessageDTO(
            role = message.role,
            text = message.text,
            imageUrl = imageUrl
        )

        val interviewDto = findById(interviewId)

        val messageList = mutableListOf<BaseMessageDTO>()

        val existingMessages = findAllMessagesByInterviewId(interviewId)
        messageList.addAll(existingMessages)
        messageList.add(compoundMessage)

        val assistantResponse = requestChatCompletion(messageList)

        existingMessages.add(
            InterviewMessageDTO(
                role = message.role,
                content = message.text,
            )
        )
        existingMessages.add(assistantResponse)

        val interview = interviewAssembler.toEntity(interviewDto, existingMessages)
        interviewRepository.save(interview)

        return assistantResponse
    }

    private fun requestChatCompletion(messages: List<BaseMessageDTO>): InterviewMessageDTO {
        val requestBody = AssistantRequestDTO(
            model = "o4-mini",
            messages = messages
        )

        val response = assistantClient.post(
            endpoint = "v1/chat/completions",
            request = requestBody,
            responseType = AssistantResponseDTO::class.java
        )

        val rawResponse = response.body ?: throw AssistantChatException("Failed to get assistant message")
        return rawResponse.getMessage()
    }

    private fun findAllMessagesByInterviewId(interviewId: UUID): MutableList<InterviewMessageDTO> {
        logger.info { "Finding all messages for interview ID: $interviewId" }
        return messageAssembler.toDtoList(interviewMessageRepository.findAllByInterviewId(interviewId).toMutableList())
    }

}