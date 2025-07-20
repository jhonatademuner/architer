package com.architer.repository.user

import com.architer.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository  : JpaRepository<User, UUID>{
    fun findByUsernameAndIsActiveTrue(username: String): User?
    fun findByEmailAndIsActiveTrue(email: String): User?
    fun existsByUsernameOrEmail(username: String, email: String): Boolean
    fun findByIdAndIsActiveTrue(id: UUID): User?
}