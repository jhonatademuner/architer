package com.architer.behavior.application

import com.architer.behavior.domain.repository.BehaviorRepository
import com.architer.behavior.presentation.dto.BehaviorCreateRequest
import com.architer.behavior.presentation.dto.BehaviorResponse
import com.architer.behavior.presentation.dto.BehaviorSimplifiedResponse
import com.architer.behavior.presentation.dto.BehaviorUpdateRequest
import com.architer.behavior.presentation.mapper.BehaviorMapper
import com.architer.shared.exception.ResourceNotFoundException
import com.architer.shared.presentation.dto.PaginatedList
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BehaviorService(
    private val repository: BehaviorRepository,
    private val mapper: BehaviorMapper
) {
    fun create(request: BehaviorCreateRequest): BehaviorResponse {
        val behavior = mapper.toEntity(request)
        return mapper.toResponse(repository.save(behavior))
    }

    fun findAll(page: Int, size: Int): PaginatedList<BehaviorSimplifiedResponse> {
        val pageResult = repository
            .findAll(PageRequest.of(page-1, size))
            .map(mapper::toSimplifiedResponse)
        return PaginatedList.from(pageResult)
    }

    fun findById(id: UUID): BehaviorResponse {
        val behavior = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("Behavior not found") }
        return mapper.toResponse(behavior)
    }

    fun update(request: BehaviorUpdateRequest): BehaviorResponse {
        val existing = repository.findById(request.id)
            .orElseThrow { ResourceNotFoundException("Behavior not found") }

        val updated = mapper.updateEntity(request, existing)
        return mapper.toResponse(repository.save(updated))
    }

    fun delete(id: UUID) {
        repository.deleteById(id)
    }
}