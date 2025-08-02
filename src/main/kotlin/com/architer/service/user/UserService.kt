package com.architer.service.user

import com.architer.assembler.user.UserAssembler
import com.architer.dto.user.UserDTO
import com.architer.service.auth.AuthService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val authService: AuthService,
    private val userAssembler: UserAssembler
) {

    fun getCurrentUser(): UserDTO {
        val user = authService.getAuthenticatedUser()
        return userAssembler.toDto(user)
    }

    fun getCurrentUserId(): UUID {
        val user = authService.getAuthenticatedUser()
        return user.id ?: throw IllegalStateException("User ID is null")
    }

}