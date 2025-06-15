package com.archter.assembler.challenge

import com.archter.assembler.AbstractAssembler
import com.archter.domain.challenge.Challenge
import com.archter.dto.challenge.ChallengeCreateDTO
import com.archter.dto.challenge.ChallengeDTO
import com.archter.dto.challenge.ChallengeUpdateDTO
import org.springframework.stereotype.Component

@Component
class ChallengeAssembler : AbstractAssembler<Challenge, ChallengeDTO>() {

    override fun toDto(entity: Challenge): ChallengeDTO {
        return ChallengeDTO(
            id = entity.id,
            title = entity.title,
            overview = entity.overview,
            content = entity.content,
            updatedAt = entity.updatedAt!!,
            createdAt = entity.createdAt!!
        )
    }

    override fun toEntity(dto: ChallengeDTO): Challenge {
        return Challenge(
            id = dto.id,
            title = dto.title,
            overview = dto.overview,
            content = dto.content
        )
    }

    fun updateEntityFromDto(dto: ChallengeUpdateDTO, entity: Challenge): Challenge {
        entity.title = dto.title
        entity.overview = dto.overview
        entity.content = dto.content
        return entity
    }

    fun toEntity(dto: ChallengeCreateDTO): Challenge {
        return Challenge(
            title = dto.title,
            overview = dto.overview,
            content = dto.content
        )
    }

}