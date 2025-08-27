package com.architer.challenge.presentation

import com.architer.challenge.application.ChallengeService
import com.architer.challenge.presentation.dto.ChallengeCreateRequest
import com.architer.challenge.presentation.dto.ChallengeResponse
import com.architer.challenge.presentation.dto.ChallengeUpdateRequest
import com.architer.challenge.presentation.dto.ChallengeSimplifiedResponse
import com.architer.shared.model.PaginatedList
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/challenges")
class ChallengeController(
    private val service: ChallengeService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: ChallengeCreateRequest): ChallengeResponse {
        return service.create(request)
    }

    @GetMapping
    fun findAll(
        @RequestParam page: Int = 1,
        @RequestParam size: Int = 10
    ): PaginatedList<ChallengeSimplifiedResponse> {
        return service.findAll(page, size)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): ChallengeResponse {
        return service.findById(id)
    }

    @PutMapping
    fun update(@RequestBody request: ChallengeUpdateRequest): ChallengeResponse {
        return service.update(request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}