package com.architer.assembler.interview

import com.architer.assembler.AbstractAssembler
import com.architer.domain.interview.message.InterviewMessage
import com.architer.dto.interview.message.InterviewMessageDTO
import com.architer.repository.interview.InterviewRepository
import com.architer.utils.exception.ResourceNotFoundException
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

}