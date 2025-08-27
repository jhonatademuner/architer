package com.architer.user.infrastructure

import com.architer.shared.exception.PasswordStrengthException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordHelper(
    private val passwordEncoder: PasswordEncoder
) {
    fun validateStrength(password: String) {
        val minLength = 8
        val hasUppercase = Regex("[A-Z]")
        val hasDigit = Regex("\\d")
        val hasSpecial = Regex("[!@#\$%^&*()\\-+=\\[\\]{};:'\"\\\\|,.<>/?]")

        when {
            password.length < minLength ->
                throw PasswordStrengthException("Password must be at least $minLength characters long")
            !hasUppercase.containsMatchIn(password) ->
                throw PasswordStrengthException("Password must contain at least one uppercase letter")
            !hasDigit.containsMatchIn(password) ->
                throw PasswordStrengthException("Password must contain at least one number")
            !hasSpecial.containsMatchIn(password) ->
                throw PasswordStrengthException("Password must contain at least one special character")
        }
    }

    fun encode(password: String): String {
        return passwordEncoder.encode(password)
    }
}