package com.architer.security

import com.architer.domain.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class CustomUserDetails(
    private val user: User
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val role = if (user.isAdmin) "ROLE_ADMIN" else "ROLE_USER"
        return listOf(SimpleGrantedAuthority(role))
    }

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = user.isActive

    fun getId(): UUID? = user.id

    fun getEmail(): String = user.email

    fun isAdmin(): Boolean = user.isAdmin

    fun getDomainUser(): User = user
}
