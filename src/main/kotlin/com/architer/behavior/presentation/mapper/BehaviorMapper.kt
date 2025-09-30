package com.architer.behavior.presentation.mapper

import com.architer.behavior.domain.model.Behavior
import com.architer.behavior.presentation.dto.BehaviorCreateRequest
import com.architer.behavior.presentation.dto.BehaviorResponse
import com.architer.behavior.presentation.dto.BehaviorSimplifiedResponse
import com.architer.behavior.presentation.dto.BehaviorUpdateRequest
import org.springframework.stereotype.Component

@Component
class BehaviorMapper {

    fun toEntity(request: BehaviorCreateRequest): Behavior {
        return Behavior(
            externalId = request.externalId,
            icon = request.icon,
            title = request.title,
            overview = request.overview,
            content = request.content,
            description = request.description,
            published = request.published
        )
    }

    fun updateEntity(request: BehaviorUpdateRequest, entity: Behavior): Behavior {
        return entity.apply {
            externalId = request.externalId
            icon = request.icon
            title = request.title
            overview = request.overview
            description = request.description
            content = request.content
            published = request.published
        }
    }

    fun toResponse(behavior: Behavior): BehaviorResponse {
        return BehaviorResponse(
            id = behavior.id,
            externalId = behavior.externalId,
            icon = behavior.icon,
            title = behavior.title,
            overview = behavior.overview,
            description = behavior.description,
            content = behavior.content,
            published = behavior.published
        )
    }

    fun toSimplifiedResponse(behavior: Behavior): BehaviorSimplifiedResponse {
        return BehaviorSimplifiedResponse(
            id = behavior.id,
            externalId = behavior.externalId,
            icon = behavior.icon,
            title = behavior.title,
            overview = behavior.overview,
            description = behavior.description,
        )
    }

}