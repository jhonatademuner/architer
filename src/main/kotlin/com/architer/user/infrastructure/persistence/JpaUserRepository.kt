package com.architer.user.infrastructure.persistence

import com.architer.user.domain.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface JpaUserRepository : JpaRepository<User, UUID> {
    fun findAllBy(pageable: Pageable): Page<User>
    fun findByEmail(email: String): Optional<User>
    fun existsByEmail(email: String): Boolean
}