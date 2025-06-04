package com.archter.controller.challenge

import com.archter.domain.challenge.Challenge
import com.archter.service.challenge.ChallengeService
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/challenge")
class ChallengeController(
    private val challengeService: ChallengeService
) {

    @PostMapping
    fun create(@RequestBody challenge: Challenge) {
        challengeService.create(challenge)
    }

    @GetMapping
    fun findAll(@RequestParam page: Int = 1, @RequestParam resultsPerPage: Int = 10): List<Challenge> {
        return challengeService.findAll(page, resultsPerPage)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): Challenge? {
        return challengeService.findById(id)
    }

    @PutMapping
    fun update(@RequestBody challenge: Challenge) {
        challengeService.update(challenge)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        challengeService.delete(id)
    }

}