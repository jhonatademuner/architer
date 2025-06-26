package com.architer.service.challenge

import com.architer.assembler.challenge.ChallengeAssembler
import com.architer.dto.challenge.ChallengeCreateDTO
import com.architer.dto.challenge.ChallengeDTO
import com.architer.dto.challenge.ChallengeUpdateDTO
import com.architer.repository.challenge.ChallengeRepository
import com.architer.utils.exception.ResourceNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChallengeService(
    private val challengeRepository: ChallengeRepository,
    private val challengeAssembler: ChallengeAssembler
) {

    fun create(challenge: ChallengeCreateDTO): ChallengeDTO {
        val entity = challengeAssembler.toEntity(challenge)
        return challengeAssembler.toDto(challengeRepository.save(entity))
    }

    fun findAll(page: Int, size: Int): List<ChallengeDTO> {
        val entityList = challengeRepository.findAll(PageRequest.of(page, size)).content
        return challengeAssembler.toDtoList(entityList)
    }

    fun findById(id: UUID): ChallengeDTO {
        val entity = challengeRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Challenge with id $id not found") }
        return challengeAssembler.toDto(entity)
    }

    fun update(challenge: ChallengeUpdateDTO): ChallengeDTO {
        val existingEntity = challengeRepository.findById(challenge.id)
            .orElseThrow { ResourceNotFoundException("Challenge with id ${challenge.id} not found") }

        val updatedEntity = challengeAssembler.updateEntityFromDto(challenge, existingEntity)
        return challengeAssembler.toDto(challengeRepository.save(updatedEntity))
    }

    fun delete(id: UUID): Unit {
        challengeRepository.deleteById(id)
    }

}