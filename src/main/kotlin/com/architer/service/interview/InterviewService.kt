package com.architer.service.interview

import com.architer.assembler.interview.InterviewAssembler
import com.architer.client.assistant.AssistantClient
import com.architer.domain.interview.InterviewRole
import com.architer.dto.assistant.AssistantRequestDTO
import com.architer.dto.assistant.AssistantResponseDTO
import com.architer.dto.interview.InterviewCreateDTO
import com.architer.dto.interview.InterviewDTO
import com.architer.dto.interview.InterviewUpdateDTO
import com.architer.dto.interview.message.BaseMessageDTO
import com.architer.dto.interview.message.InterviewCompoundMessageDTO
import com.architer.dto.interview.message.InterviewMessageCreateDTO
import com.architer.dto.interview.message.InterviewMessageDTO
import com.architer.repository.assistant.behavior.AssistantBehaviorRepository
import com.architer.repository.challenge.ChallengeRepository
import com.architer.repository.interview.InterviewRepository
import com.architer.service.minio.MinioService
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
    private val interviewRepository: InterviewRepository,
    private val interviewAssembler: InterviewAssembler,
    private val assistantClient: AssistantClient,
    private val assistantBehaviorRepository: AssistantBehaviorRepository,
    private val challengeRepository: ChallengeRepository,
    private val minioService: MinioService
) {

    fun create(challengeId: UUID, assistantBehaviorId: UUID) : InterviewDTO {
        logger.info { "Creating chat - challengeId: $challengeId, assistantBehaviorId: $assistantBehaviorId" }

        val assistantBehavior = assistantBehaviorRepository.findById(assistantBehaviorId)
            .orElseThrow { ResourceNotFoundException("AssistantBehavior with id $assistantBehaviorId not found") }

        val challenge = challengeRepository.findById(challengeId)
            .orElseThrow { ResourceNotFoundException("Challenge with id $challengeId not found") }

        val assistantBehaviorMessage = InterviewMessageDTO(
            role = InterviewRole.system,
            content = assistantBehavior.toString()
        )

        val challengeMessage = InterviewMessageDTO(
            role = InterviewRole.system,
            content = challenge.toString()
        )

        val messages = mutableListOf(assistantBehaviorMessage, challengeMessage)

        val assistantResponse = requestChatCompletion(messages)
        messages.add(assistantResponse)

        val interviewDto = InterviewCreateDTO(
            title = challenge.title,
            messages = messages,
            assistantBehaviorId = assistantBehavior.id,
            challengeId = challenge.id
        )

        val interview = interviewAssembler.toEntity(interviewDto)

        return interviewAssembler.toDto(interviewRepository.save(interview))
    }

    fun sendMessage(interviewId: UUID, message: InterviewMessageCreateDTO, image: MultipartFile?): InterviewMessageDTO {
        logger.info { "Sending message to chat - interviewId: $interviewId" }

        val imageUrl = if (image != null && !image.isEmpty) {
            minioService.uploadInterviewSystemDesign(interviewId, image)
        } else null

        val compoundMessage = InterviewCompoundMessageDTO(
            role = message.role,
            text = message.text,
            imageUrl = imageUrl
        )

        val interviewDto = this.findById(interviewId)

        val messageList = mutableListOf<BaseMessageDTO>()
        messageList.addAll(interviewDto.messages)
        messageList.add(compoundMessage)

        val assistantResponse = requestChatCompletion(messageList)

        interviewDto.messages.add(InterviewMessageDTO(
            role = message.role,
            content = message.text,
        ))
        interviewDto.messages.add(assistantResponse)

        val interview = interviewAssembler.toEntity(interviewDto)
        interviewRepository.save(interview)

        return assistantResponse
    }

    private fun requestChatCompletion(messages: List<BaseMessageDTO>) : InterviewMessageDTO {
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

    fun findAll(page: Int, size: Int): List<InterviewDTO> {
        logger.info { "Finding all interviews - page: $page, size: $size" }
        val entityList = interviewRepository.findAll(PageRequest.of(page, size)).content
        return interviewAssembler.toDtoList(entityList)
    }

    fun findById(id: UUID): InterviewDTO {
        logger.info { "Finding chat by ID: $id" }
        val entity = interviewRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Interview with id $id not found") }
        return interviewAssembler.toDto(entity)
    }

    fun update(interviewUpdateDTO: InterviewUpdateDTO): InterviewDTO {
        logger.info { "Updating interview - id: ${interviewUpdateDTO.id}" }
        val existingInterview = interviewRepository.findById(interviewUpdateDTO.id)
            .orElseThrow { ResourceNotFoundException("Interview with id ${interviewUpdateDTO.id} not found") }

        val updatedInterview = interviewAssembler.updateEntityFromDto(interviewUpdateDTO, existingInterview)
        return interviewAssembler.toDto(interviewRepository.save(updatedInterview))
    }

    fun delete(id: UUID): Unit {
        logger.info { "Deleting interview - id: $id" }
        interviewRepository.deleteById(id)
    }

}