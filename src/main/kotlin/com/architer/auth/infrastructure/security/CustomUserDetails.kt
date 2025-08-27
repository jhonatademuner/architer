package com.architer.auth.infrastructure.security

import com.architer.user.domain.model.User
import com.architer.user.domain.model.UserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class CustomUserDetails(
    private val user: User
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val role = if (user.role == UserRole.ADMIN) "ROLE_ADMIN" else "ROLE_USER"
        return listOf(SimpleGrantedAuthority(role))
    }

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = !user.isDeleted()

    fun getId(): UUID? = user.id

    fun getEmail(): String = user.email

    fun getName(): String = user.name

    fun getRole(): UserRole = user.role

    fun getDomainUser(): User = user
}