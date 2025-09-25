package com.architer.challenge.presentation.mapper

import com.architer.challenge.domain.model.Challenge
import com.architer.challenge.presentation.dto.ChallengeCreateRequest
import com.architer.challenge.presentation.dto.ChallengeResponse
import com.architer.challenge.presentation.dto.ChallengeUpdateRequest
import com.architer.challenge.presentation.dto.ChallengeSimplifiedResponse
import org.springframework.stereotype.Component

@Component
class ChallengeMapper {

    fun toEntity(request: ChallengeCreateRequest): Challenge {
        return Challenge(
            externalId = request.externalId,
            icon = request.icon,
            title = request.title,
            overview = request.overview,
            description = request.description,
            content = request.content,
            category = request.category,
            difficulty = request.difficulty,
        )
    }

    fun updateEntity(request: ChallengeUpdateRequest, entity: Challenge): Challenge {
        return entity.apply {
            externalId = request.externalId
            icon = request.icon
            title = request.title
            overview = request.overview
            description = request.description
            content = request.content
            category = request.category
            difficulty = request.difficulty
        }
    }

    fun toResponse(challenge: Challenge): ChallengeResponse {
        return ChallengeResponse(
            id = challenge.id,
            externalId = challenge.externalId,
            icon = challenge.icon,
            title = challenge.title,
            overview = challenge.overview,
            description = challenge.description,
            content = challenge.content,
            category = challenge.category,
            difficulty = challenge.difficulty,
        )
    }

    fun toSimplifiedResponse(challenge: Challenge): ChallengeSimplifiedResponse {
        return ChallengeSimplifiedResponse(
            id = challenge.id,
            externalId = challenge.externalId,
            icon = challenge.icon,
            title = challenge.title,
            overview = challenge.overview,
            description = challenge.description,
            category = challenge.category,
            difficulty = challenge.difficulty,
        )
    }
}