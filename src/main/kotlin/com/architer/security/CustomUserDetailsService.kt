package com.architer.security

import com.architer.repository.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsernameAndIsActiveTrue(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")
        return CustomUserDetails(user)
    }

    fun loadUserById(userId: String): UserDetails {
        val user = userRepository.findByIdAndIsActiveTrue(UUID.fromString(userId))
            ?: throw UsernameNotFoundException("User not found")
        return CustomUserDetails(user)
    }
}
