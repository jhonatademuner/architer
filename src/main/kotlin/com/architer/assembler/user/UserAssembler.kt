package com.architer.assembler.user

import com.architer.assembler.AbstractAssembler
import com.architer.domain.user.User
import com.architer.dto.auth.UserRegisterDTO
import com.architer.dto.user.UserDTO
import org.springframework.stereotype.Component

@Component
class UserAssembler {
    fun toDto(entity: User): UserDTO {
        return UserDTO(
            id = entity.id,
            username = entity.username,
            email = entity.email,
            isAdmin = entity.isAdmin,
            isActive = entity.isActive
        )
    }

    fun updateEntityFromDto(dto: UserDTO, entity: User): User {
        entity.username = dto.username
        entity.email = dto.email
        entity.isAdmin = dto.isAdmin ?: entity.isAdmin
        entity.isActive = dto.isActive ?: entity.isActive
        return entity
    }

    fun toEntity(dto: UserRegisterDTO): User {
        return User(
            username = dto.username,
            email = dto.email,
            password = dto.password,
        )
    }

}