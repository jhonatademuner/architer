package com.architer.assembler.interview

import com.architer.assembler.AbstractAssembler
import com.architer.assembler.behavior.BehaviorAssembler
import com.architer.assembler.challenge.ChallengeAssembler
import com.architer.domain.interview.Interview
import com.architer.dto.interview.InterviewDTO
import com.architer.dto.interview.SimplifiedInterviewDTO
import com.architer.dto.interview.message.InterviewMessageDTO
import com.architer.repository.behavior.BehaviorRepository
import com.architer.repository.challenge.ChallengeRepository
import com.architer.service.auth.AuthService
import com.architer.utils.exception.ResourceNotFoundException
import org.springframework.stereotype.Component

@Component
class InterviewAssembler(
    private val behaviorRepository: BehaviorRepository,
    private val challengeRepository: ChallengeRepository,
    private val messageAssembler: InterviewMessageAssembler,
    private val challengeAssembler: ChallengeAssembler,
    private val behaviorAssembler: BehaviorAssembler,
    private val authService: AuthService
) : AbstractAssembler<Interview, InterviewDTO>() {

    override fun toDto(entity: Interview): InterviewDTO {
        return InterviewDTO(
            id = entity.id,
            title = entity.title,
            timeSpent = entity.timeSpent,
            feedback = entity.feedback,
            behavior = entity.behavior.let { behaviorAssembler.toDto(it) },
            challenge = entity.challenge.let { challengeAssembler.toDto(it) },
            seniority = entity.seniority,
            score = entity.score,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    override fun toEntity(dto: InterviewDTO): Interview {
        val behavior = dto.behavior?.id?.let {
            behaviorRepository.findById(it)
                .orElseThrow { ResourceNotFoundException("AssistantBehavior with id $it not found") }
        } ?: throw IllegalArgumentException("assistantBehavior is required")

        val challenge = dto.challenge?.id?.let {
            challengeRepository.findById(it)
                .orElseThrow { ResourceNotFoundException("Challenge with id $it not found") }
        } ?: throw IllegalArgumentException("challenge is required")

        val userId = dto.user ?: authService.getAuthenticatedUser().id
            ?: throw IllegalStateException("User ID is null")

        val interview = Interview(
            id = dto.id,
            title = dto.title,
            timeSpent = dto.timeSpent,
            feedback = dto.feedback,
            behavior = behavior,
            challenge = challenge,
            seniority = dto.seniority,
            score = dto.score,
            userId = userId
        )

        return interview
    }

    fun toEntity(dto: InterviewDTO, messages: MutableList<InterviewMessageDTO>): Interview {
        val behavior = dto.behavior?.id?.let {
            behaviorRepository.findById(it)
                .orElseThrow { ResourceNotFoundException("AssistantBehavior with id $it not found") }
        } ?: throw IllegalArgumentException("assistantBehavior is required")

        val challenge = dto.challenge?.id?.let {
            challengeRepository.findById(it)
                .orElseThrow { ResourceNotFoundException("Challenge with id $it not found") }
        } ?: throw IllegalArgumentException("challenge is required")

        val userId = dto.user ?: authService.getAuthenticatedUser().id
        ?: throw IllegalStateException("User ID is null")

        val interview = Interview(
            id = dto.id,
            title = dto.title,
            timeSpent = dto.timeSpent,
            feedback = dto.feedback,
            behavior = behavior,
            challenge = challenge,
            seniority = dto.seniority,
            score = dto.score,
            userId = userId
        )

        messageAssembler.toEntityList(messages)
            .forEach { interview.addMessage(it) }

        return interview
    }

    fun toSimplifiedDto(entity: Interview): SimplifiedInterviewDTO {
        return SimplifiedInterviewDTO(
            id = entity.id,
            title = entity.title,
            timeSpent = entity.timeSpent,
            score = entity.score,
            behaviorTitle = entity.behavior.title,
            challengeTitle = entity.challenge.title,
            seniority = entity.seniority.displayName,
            createdAt = entity.createdAt
        )
    }

    fun toSimplifiedDtoList(entities: List<Interview>): List<SimplifiedInterviewDTO> {
        return entities.map { toSimplifiedDto(it) }
    }

}
