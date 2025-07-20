package com.architer.service.behavior

import com.architer.assembler.behavior.BehaviorAssembler
import com.architer.dto.behavior.BehaviorCreateDTO
import com.architer.dto.behavior.BehaviorDTO
import com.architer.dto.behavior.BehaviorUpdateDTO
import com.architer.repository.behavior.BehaviorRepository
import com.architer.utils.exception.ResourceNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class BehaviorService(
    private val behaviorRepository: BehaviorRepository,
    private val behaviorAssembler: BehaviorAssembler
) {

    fun create(dto: BehaviorCreateDTO) : BehaviorDTO {
        val entity = behaviorAssembler.toEntity(dto)
        return behaviorAssembler.toDto(behaviorRepository.save(entity))
    }

    fun findAll(page: Int, size: Int): List<BehaviorDTO> {
        val entityList = behaviorRepository.findAll(PageRequest.of(page, size)).content
        return behaviorAssembler.toDtoList(entityList)
    }

    fun findById(id: UUID): BehaviorDTO {
        val entity = behaviorRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Behavior with id $id not found") }
        return behaviorAssembler.toDto(entity)
    }

    fun update(dto: BehaviorUpdateDTO): BehaviorDTO {
        val existingEntity = behaviorRepository.findById(dto.id)
            .orElseThrow { ResourceNotFoundException("Behavior with id ${dto.id} not found") }

        val updatedEntity = behaviorAssembler.updateEntityFromDto(dto, existingEntity)
        return behaviorAssembler.toDto(behaviorRepository.save(updatedEntity))
    }

    fun delete(id: UUID): Unit {
        behaviorRepository.deleteById(id)
    }

}