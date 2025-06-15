package com.archter.service.challenge

import com.archter.assembler.challenge.ChallengeAssembler
import com.archter.dto.challenge.ChallengeCreateDTO
import com.archter.dto.challenge.ChallengeDTO
import com.archter.dto.challenge.ChallengeUpdateDTO
import com.archter.repository.challenge.ChallengeRepository
import com.archter.utils.exception.ResourceNotFoundException
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