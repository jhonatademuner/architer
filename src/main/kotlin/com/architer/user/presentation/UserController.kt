package com.architer.user.presentation

import com.architer.shared.model.PaginatedList
import com.architer.user.application.UserService
import com.architer.user.presentation.dto.UserCreateRequest
import com.architer.user.presentation.dto.UserResponse
import com.architer.user.presentation.dto.UserUpdateRequest
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
@RequestMapping("/api/v1/users")
class UserController(
    private val service: UserService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: UserCreateRequest): UserResponse {
        return service.create(request)
    }

    @GetMapping
    fun findAll(
        @RequestParam page: Int = 1,
        @RequestParam size: Int = 10
    ): PaginatedList<UserResponse> {
        return service.findAll(page, size)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): UserResponse {
        return service.findById(id)
    }

    @PutMapping
    fun update(@RequestBody request: UserUpdateRequest): UserResponse {
        return service.update(request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}