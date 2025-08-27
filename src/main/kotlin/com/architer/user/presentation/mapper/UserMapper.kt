package com.architer.user.presentation.mapper

import com.architer.user.domain.model.User
import com.architer.user.presentation.dto.UserCreateRequest
import com.architer.user.presentation.dto.UserResponse
import com.architer.user.presentation.dto.UserUpdateRequest
import org.springframework.stereotype.Component

@Component
class UserMapper {

    fun toEntity(request: UserCreateRequest): User {
        return User(
            name = request.name,
            email = request.email,
            password = request.password,
        )
    }

    fun toEntity(name: String, email: String, password: String): User {
        return User(
            name = name,
            email = email,
            password = password,
        )
    }

    fun updateEntity(request: UserUpdateRequest, entity: User): User {
        return entity.apply {
            name = request.name
            email = request.email
            password = request.password
        }
    }

    fun toResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id!!,
            name = user.name,
            email = user.email,
        )
    }
}