package com.architer.assembler.interview

import com.architer.assembler.AbstractAssembler
import com.architer.assembler.behavior.BehaviorAssembler
import com.architer.assembler.challenge.ChallengeAssembler
import com.architer.domain.interview.Interview
import com.architer.dto.interview.InterviewDTO
import com.architer.dto.interview.InterviewUpdateDTO
import com.architer.repository.behavior.BehaviorRepository
import com.architer.repository.challenge.ChallengeRepository
import com.architer.utils.exception.ResourceNotFoundException
import org.springframework.stereotype.Component

@Component
class InterviewAssembler(
    private val behaviorRepository: BehaviorRepository,
    private val challengeRepository: ChallengeRepository,
    private val messageAssembler: InterviewMessageAssembler,
    private val challengeAssembler: ChallengeAssembler,
    private val behaviorAssembler: BehaviorAssembler,
) : AbstractAssembler<Interview, InterviewDTO>() {

    override fun toDto(entity: Interview): InterviewDTO {
        return InterviewDTO(
            id = entity.id,
            title = entity.title,
            timeSpent = entity.timeSpent,
            feedback = entity.feedback,
            messages = messageAssembler.toDtoList(entity.messages),
            assistantBehavior = entity.behavior?.let { behaviorAssembler.toDto(it) },
            challenge = entity.challenge?.let { challengeAssembler.toDto(it) },
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    override fun toEntity(dto: InterviewDTO): Interview {
        val assistantBehavior = dto.assistantBehavior?.id?.let {
            behaviorRepository.findById(it)
                .orElseThrow { ResourceNotFoundException("AssistantBehavior with id $it not found") }
        }

        val challenge = dto.challenge?.id?.let {
            challengeRepository.findById(it)
                .orElseThrow { ResourceNotFoundException("Challenge with id $it not found") }
        }

        val interview = Interview(
            id = dto.id,
            title = dto.title,
            timeSpent = dto.timeSpent,
            feedback = dto.feedback,
            behavior = assistantBehavior,
            challenge = challenge,
        )

        messageAssembler.toEntityList(dto.messages)
            .forEach { interview.addMessage(it) }

        return interview
    }

    fun updateEntityFromDto(dto: InterviewUpdateDTO, entity: Interview): Interview {
        entity.title = dto.title
        entity.timeSpent = dto.timeSpent
        entity.feedback = dto.feedback
        return entity
    }

}
