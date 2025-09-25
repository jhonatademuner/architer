package com.architer.challenge.application

import com.architer.challenge.domain.repository.ChallengeRepository
import com.architer.challenge.presentation.dto.ChallengeCreateRequest
import com.architer.challenge.presentation.dto.ChallengeResponse
import com.architer.challenge.presentation.dto.ChallengeUpdateRequest
import com.architer.challenge.presentation.dto.ChallengeSimplifiedResponse
import com.architer.challenge.presentation.mapper.ChallengeMapper
import com.architer.shared.exception.ResourceNotFoundException
import com.architer.shared.presentation.dto.PaginatedList
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChallengeService(
    private val repository: ChallengeRepository,
    private val mapper: ChallengeMapper
) {
    fun create(request: ChallengeCreateRequest): ChallengeResponse {
        val challenge = mapper.toEntity(request)
        return mapper.toResponse(repository.save(challenge))
    }

    fun findAll(page: Int, size: Int): PaginatedList<ChallengeSimplifiedResponse> {
        val pageResult = repository
            .findAll(PageRequest.of(page-1, size))
            .map(mapper::toSimplifiedResponse)
        return PaginatedList.from(pageResult)
    }

    fun findById(id: UUID): ChallengeResponse {
        val challenge = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("Challenge not found") }
        return mapper.toResponse(challenge)
    }

    fun update(request: ChallengeUpdateRequest): ChallengeResponse {
        val existing = repository.findById(request.id)
            .orElseThrow { ResourceNotFoundException("Challenge not found") }

        val updated = mapper.updateEntity(request, existing)
        return mapper.toResponse(repository.save(updated))
    }

    fun delete(id: UUID) {
        repository.deleteById(id)
    }
}