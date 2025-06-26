package com.architer.assembler.assistant

import com.architer.assembler.AbstractAssembler
import com.architer.domain.assistant.behavior.AssistantBehavior
import com.architer.dto.assistant.behavior.AssistantBehaviorCreateDTO
import com.architer.dto.assistant.behavior.AssistantBehaviorDTO
import com.architer.dto.assistant.behavior.AssistantBehaviorUpdateDTO
import org.springframework.stereotype.Component

@Component
class AssistantBehaviorAssembler : AbstractAssembler<AssistantBehavior, AssistantBehaviorDTO>() {

    override fun toDto(entity: AssistantBehavior): AssistantBehaviorDTO {
        return AssistantBehaviorDTO(
            id = entity.id,
            title = entity.title,
            overview = entity.overview,
            content = entity.content,
            updatedAt = entity.updatedAt,
            createdAt = entity.createdAt
        )
    }

    override fun toEntity(dto: AssistantBehaviorDTO): AssistantBehavior {
        return AssistantBehavior(
            id = dto.id,
            title = dto.title,
            overview = dto.overview,
            content = dto.content,
        )
    }

    fun updateEntityFromDto(dto: AssistantBehaviorUpdateDTO, entity: AssistantBehavior): AssistantBehavior {
        entity.title = dto.title
        entity.overview = dto.overview
        entity.content = dto.content
        return entity
    }

    fun toEntity(dto: AssistantBehaviorCreateDTO): AssistantBehavior {
        return AssistantBehavior(
            title = dto.title,
            overview = dto.overview,
            content = dto.content
        )
    }

}