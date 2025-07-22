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
            name = entity.name,
            email = entity.email,
            isAdmin = entity.isAdmin,
            isActive = entity.isActive
        )
    }

    fun updateEntityFromDto(dto: UserDTO, entity: User): User {
        entity.name = dto.name
        entity.email = dto.email
        entity.isAdmin = dto.isAdmin ?: entity.isAdmin
        entity.isActive = dto.isActive ?: entity.isActive
        return entity
    }

    fun toEntity(dto: UserRegisterDTO): User {
        return User(
            name = dto.name,
            email = dto.email,
            password = dto.password,
        )
    }

}