package com.architer.assembler.behavior

import com.architer.assembler.AbstractAssembler
import com.architer.domain.behavior.Behavior
import com.architer.dto.behavior.BehaviorCreateDTO
import com.architer.dto.behavior.BehaviorDTO
import com.architer.dto.behavior.BehaviorUpdateDTO
import org.springframework.stereotype.Component

@Component
class BehaviorAssembler : AbstractAssembler<Behavior, BehaviorDTO>() {

    override fun toDto(entity: Behavior): BehaviorDTO {
        return BehaviorDTO(
            id = entity.id,
            title = entity.title,
            overview = entity.overview,
            content = entity.content,
            updatedAt = entity.updatedAt,
            createdAt = entity.createdAt
        )
    }

    override fun toEntity(dto: BehaviorDTO): Behavior {
        return Behavior(
            id = dto.id,
            title = dto.title,
            overview = dto.overview,
            content = dto.content,
        )
    }

    fun updateEntityFromDto(dto: BehaviorUpdateDTO, entity: Behavior): Behavior {
        entity.title = dto.title
        entity.overview = dto.overview
        entity.content = dto.content
        return entity
    }

    fun toEntity(dto: BehaviorCreateDTO): Behavior {
        return Behavior(
            title = dto.title,
            overview = dto.overview,
            content = dto.content
        )
    }

}