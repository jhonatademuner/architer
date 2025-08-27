package com.architer.auth.infrastructure

import com.architer.auth.infrastructure.security.CustomUserDetails
import com.architer.shared.application.CurrentUserHelper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CurrentUserHelperAdapter: CurrentUserHelper {
    override fun getCurrentUserId(): UUID {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw IllegalStateException("No authenticated user found!")

        val principal = authentication.principal

        return when (principal) {
            is CustomUserDetails -> principal.getId() ?: throw IllegalStateException("User ID missing")
            else -> throw IllegalStateException("Invalid principal type")
        }
    }
}