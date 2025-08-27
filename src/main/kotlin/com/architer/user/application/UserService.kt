package com.architer.user.application

import com.architer.shared.exception.ResourceNotFoundException
import com.architer.shared.model.PaginatedList
import com.architer.user.domain.repository.UserRepository
import com.architer.user.infrastructure.PasswordHelper
import com.architer.user.presentation.dto.UserCreateRequest
import com.architer.user.presentation.dto.UserResponse
import com.architer.user.presentation.dto.UserUpdateRequest
import com.architer.user.presentation.mapper.UserMapper
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val repository: UserRepository,
    private val mapper: UserMapper,
    private val passwordHelper: PasswordHelper
) {
    fun create(request: UserCreateRequest): UserResponse {
        passwordHelper.validateStrength(request.password)
        val encodedPassword = passwordHelper.encode(request.password)
        val user = mapper.toEntity(request.name, request.email, encodedPassword)
        return mapper.toResponse(repository.save(user))
    }

    fun findAll(page: Int, size: Int): PaginatedList<UserResponse> {
        val pageResult = repository
            .findAll(PageRequest.of(page-1, size))
            .map(mapper::toResponse)
        return PaginatedList.from(pageResult)
    }

    fun findById(id: UUID): UserResponse {
        val user = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found") }
        return mapper.toResponse(user)
    }

    fun findByEmail(email: String): UserResponse {
        val user = repository.findByEmail(email)
            .orElseThrow { ResourceNotFoundException("User not found") }
        return mapper.toResponse(user)
    }

    fun existsByEmail(email: String): Boolean {
        return repository.existsByEmail(email)
    }

    fun update(request: UserUpdateRequest): UserResponse {
        val existing = repository.findById(request.id)
            .orElseThrow { ResourceNotFoundException("User not found") }

        passwordHelper.validateStrength(request.password)
        request.password = passwordHelper.encode(request.password)
        val updated = mapper.updateEntity(request, existing)
        return mapper.toResponse(repository.save(updated))
    }

    fun delete(id: UUID) {
        repository.deleteById(id)
    }
}