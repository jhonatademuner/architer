package com.architer.repository.user

import com.architer.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository  : JpaRepository<User, UUID>{
    fun findByEmailAndIsActiveTrue(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun findByIdAndIsActiveTrue(id: UUID): User?
}