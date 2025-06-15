package com.archter.assembler.interview

import com.archter.assembler.AbstractAssembler
import com.archter.domain.interview.message.InterviewMessage
import com.archter.dto.interview.message.InterviewMessageCreateDTO
import com.archter.dto.interview.message.InterviewMessageDTO
import com.archter.repository.interview.InterviewRepository
import com.archter.utils.exception.ResourceNotFoundException
import org.springframework.stereotype.Component

@Component
class InterviewMessageAssembler(
    private val interviewRepository: InterviewRepository
) : AbstractAssembler<InterviewMessage, InterviewMessageDTO>() {

    override fun toDto(entity: InterviewMessage): InterviewMessageDTO {
        return InterviewMessageDTO(
            id = entity.id,
            role = entity.role,
            content = entity.content,
            interviewId = entity.interview?.id,
            createdAt = entity.createdAt,
        )
    }

    override fun toEntity(dto: InterviewMessageDTO): InterviewMessage {
        val interview = dto.interviewId?.let {
            interviewRepository.findById(it)
                .orElseThrow { ResourceNotFoundException("Interview with id $it not found") }
        }

        return InterviewMessage(
            id = dto.id,
            role = dto.role,
            content = dto.content,
            interview = interview,
            createdAt = dto.createdAt,
        )
    }

    fun toEntity(dto: InterviewMessageCreateDTO): InterviewMessage {
        return InterviewMessage(
            role = dto.role,
            content = dto.content,
        )
    }

}