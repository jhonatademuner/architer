package com.architer.service.assistant.behavior

import com.architer.assembler.assistant.AssistantBehaviorAssembler
import com.architer.dto.assistant.behavior.AssistantBehaviorCreateDTO
import com.architer.dto.assistant.behavior.AssistantBehaviorDTO
import com.architer.dto.assistant.behavior.AssistantBehaviorUpdateDTO
import com.architer.repository.assistant.behavior.AssistantBehaviorRepository
import com.architer.utils.exception.ResourceNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class AssistantBehaviorService(
    private val assistantBehaviorRepository: AssistantBehaviorRepository,
    private val assistantBehaviorAssembler: AssistantBehaviorAssembler
) {

    fun create(dto: AssistantBehaviorCreateDTO) : AssistantBehaviorDTO {
        val entity = assistantBehaviorAssembler.toEntity(dto)
        return assistantBehaviorAssembler.toDto(assistantBehaviorRepository.save(entity))
    }

    fun findAll(page: Int, size: Int): List<AssistantBehaviorDTO> {
        val entityList = assistantBehaviorRepository.findAll(PageRequest.of(page, size)).content
        return assistantBehaviorAssembler.toDtoList(entityList)
    }

    fun findById(id: UUID): AssistantBehaviorDTO {
        val entity = assistantBehaviorRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Behavior with id $id not found") }
        return assistantBehaviorAssembler.toDto(entity)
    }

    fun update(dto: AssistantBehaviorUpdateDTO): AssistantBehaviorDTO {
        val existingEntity = assistantBehaviorRepository.findById(dto.id)
            .orElseThrow { ResourceNotFoundException("Behavior with id ${dto.id} not found") }

        val updatedEntity = assistantBehaviorAssembler.updateEntityFromDto(dto, existingEntity)
        return assistantBehaviorAssembler.toDto(assistantBehaviorRepository.save(updatedEntity))
    }

    fun delete(id: UUID): Unit {
        assistantBehaviorRepository.deleteById(id)
    }

}