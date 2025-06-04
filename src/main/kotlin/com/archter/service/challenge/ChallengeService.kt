package com.archter.service.challenge

import com.archter.domain.challenge.Challenge
import com.archter.repository.challenge.ChallengeRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChallengeService(
    private val challengeRepository: ChallengeRepository,
) {

    fun create(challenge: Challenge) {
        challengeRepository.save(challenge)
    }

    fun findAll(page: Int, resultsPerPage: Int): List<Challenge> {
        val pageable = PageRequest.of(page-1, resultsPerPage)
        val challenges = challengeRepository.findAll(pageable)
        return challenges.content
    }

    fun findById(id: UUID): Challenge {
        return challengeRepository.findById(id).orElse(null)
    }

    fun update(challenge: Challenge): Challenge? {
        val existingChallenge = challengeRepository.findById(challenge.id).orElse(null)
        return if (existingChallenge != null) {
            challengeRepository.save(challenge)
        } else {
            null
        }
    }

    fun delete(id: UUID) {
        challengeRepository.deleteById(id)
    }

}