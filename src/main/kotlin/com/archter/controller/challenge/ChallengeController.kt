package com.archter.controller.challenge

import com.archter.dto.challenge.ChallengeCreateDTO
import com.archter.dto.challenge.ChallengeDTO
import com.archter.dto.challenge.ChallengeUpdateDTO
import com.archter.service.challenge.ChallengeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api")
class ChallengeController(
    private val challengeService: ChallengeService
) {

    @PostMapping("/v1/challenge")
    fun create(@RequestBody challenge: ChallengeCreateDTO): ResponseEntity<ChallengeDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(challengeService.create(challenge))
    }

    @GetMapping("/v1/challenge")
    fun findAll(@RequestParam page: Int = 0, @RequestParam size: Int = 10): ResponseEntity<List<ChallengeDTO>> {
        return ResponseEntity.status(HttpStatus.OK).body(challengeService.findAll(page, size))
    }

    @GetMapping("/v1/challenge/{id}")
    fun findById(@PathVariable id: UUID): ResponseEntity<ChallengeDTO> {
        return ResponseEntity.status(HttpStatus.OK).body(challengeService.findById(id))
    }

    @PutMapping("/v1/challenge")
    fun update(@RequestBody challenge: ChallengeUpdateDTO): ResponseEntity<ChallengeDTO> {
        return ResponseEntity.status(HttpStatus.OK).body(challengeService.update(challenge))
    }

    @DeleteMapping("/v1/challenge/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        challengeService.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

}