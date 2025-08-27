package com.architer.user.infrastructure.persistence

import com.architer.user.domain.model.User
import com.architer.user.domain.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
class UserRepositoryAdapter(
    private val jpaRepository: JpaUserRepository
) : UserRepository {
    override fun save(user: User): User = jpaRepository.save(user)
    override fun findAll(pageable: Pageable): Page<User> = jpaRepository.findAllBy(pageable)
    override fun findById(id: UUID): Optional<User> = jpaRepository.findById(id)
    override fun deleteById(id: UUID) = jpaRepository.deleteById(id)
    override fun findByEmail(email: String): Optional<User> = jpaRepository.findByEmail(email)
    override fun existsByEmail(email: String): Boolean = jpaRepository.existsByEmail(email)
}