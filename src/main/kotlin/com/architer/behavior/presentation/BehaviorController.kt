package com.architer.behavior.presentation

import com.architer.behavior.application.BehaviorService
import com.architer.behavior.presentation.dto.BehaviorCreateRequest
import com.architer.behavior.presentation.dto.BehaviorResponse
import com.architer.behavior.presentation.dto.BehaviorSimplifiedResponse
import com.architer.behavior.presentation.dto.BehaviorUpdateRequest
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
@RequestMapping("/api/v1/behaviors")
class BehaviorController(
    private val service: BehaviorService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: BehaviorCreateRequest): BehaviorResponse {
        return service.create(request)
    }

    @GetMapping
    fun findAll(
        @RequestParam page: Int = 1,
        @RequestParam size: Int = 10
    ): PaginatedList<BehaviorSimplifiedResponse> {
        return service.findAll(page, size)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): BehaviorResponse {
        return service.findById(id)
    }

    @PutMapping
    fun update(@RequestBody request: BehaviorUpdateRequest): BehaviorResponse {
        return service.update(request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}