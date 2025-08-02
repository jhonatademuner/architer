package com.architer.assembler.behavior

import com.architer.assembler.AbstractAssembler
import com.architer.domain.behavior.Behavior
import com.architer.dto.behavior.BehaviorCreateDTO
import com.architer.dto.behavior.BehaviorDTO
import com.architer.dto.behavior.BehaviorUpdateDTO
import com.architer.dto.behavior.SimplifiedBehaviorDTO
import org.springframework.stereotype.Component

@Component
class BehaviorAssembler : AbstractAssembler<Behavior, BehaviorDTO>() {

    override fun toDto(entity: Behavior): BehaviorDTO {
        return BehaviorDTO(
            id = entity.id,
            title = entity.title,
            overview = entity.overview,
            content = entity.content,
            icon = entity.icon
        )
    }

    override fun toEntity(dto: BehaviorDTO): Behavior {
        return Behavior(
            id = dto.id,
            title = dto.title,
            overview = dto.overview,
            content = dto.content,
            icon = dto.icon
        )
    }

    fun updateEntityFromDto(dto: BehaviorUpdateDTO, entity: Behavior): Behavior {
        entity.title = dto.title
        entity.overview = dto.overview
        entity.content = dto.content
        entity.icon = dto.icon
        return entity
    }

    fun toEntity(dto: BehaviorCreateDTO): Behavior {
        return Behavior(
            title = dto.title,
            overview = dto.overview,
            content = dto.content,
            icon = dto.icon
        )
    }

    fun toSimplifiedDto(entity: Behavior): SimplifiedBehaviorDTO {
        return SimplifiedBehaviorDTO(
            id = entity.id,
            title = entity.title,
            overview = entity.overview,
            icon = entity.icon
        )
    }

    fun toSimplifiedDtoList(entities: List<Behavior>): List<SimplifiedBehaviorDTO> {
        return entities.map { toSimplifiedDto(it) }
    }

}