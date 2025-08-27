package com.architer.user.domain.repository

import com.architer.user.domain.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

interface UserRepository {
    fun save(user: User) : User
    fun findAll(pageable: Pageable): Page<User>
    fun findById(id: UUID): Optional<User>
    fun findByEmail(email: String): Optional<User>
    fun deleteById(id: UUID)
    fun existsByEmail(email: String): Boolean
}