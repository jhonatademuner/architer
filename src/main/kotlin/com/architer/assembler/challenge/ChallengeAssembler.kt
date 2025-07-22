package com.architer.assembler.challenge

import com.architer.assembler.AbstractAssembler
import com.architer.domain.challenge.Challenge
import com.architer.domain.challenge.ChallengeCategory
import com.architer.domain.challenge.ChallengeDifficulty
import com.architer.dto.challenge.ChallengeCreateDTO
import com.architer.dto.challenge.ChallengeDTO
import com.architer.dto.challenge.ChallengeUpdateDTO
import org.springframework.stereotype.Component

@Component
class ChallengeAssembler : AbstractAssembler<Challenge, ChallengeDTO>() {

    override fun toDto(entity: Challenge): ChallengeDTO {
        return ChallengeDTO(
            id = entity.id,
            title = entity.title,
            overview = entity.overview,
            content = entity.content,
            category = entity.category.displayName,
            difficulty = entity.difficulty.displayName,
            icon = entity.icon
        )
    }

    override fun toEntity(dto: ChallengeDTO): Challenge {
        return Challenge(
            id = dto.id,
            title = dto.title,
            overview = dto.overview,
            content = dto.content,
            category = ChallengeCategory.valueOf(dto.category),
            difficulty = ChallengeDifficulty.valueOf(dto.difficulty),
            icon = dto.icon
        )
    }

    fun updateEntityFromDto(dto: ChallengeUpdateDTO, entity: Challenge): Challenge {
        entity.title = dto.title
        entity.overview = dto.overview
        entity.content = dto.content
        entity.category = dto.category
        entity.difficulty = dto.difficulty
        entity.icon = dto.icon
        return entity
    }

    fun toEntity(dto: ChallengeCreateDTO): Challenge {
        return Challenge(
            title = dto.title,
            overview = dto.overview,
            content = dto.content,
            category = dto.category,
            difficulty = dto.difficulty,
            icon = dto.icon
        )
    }

}