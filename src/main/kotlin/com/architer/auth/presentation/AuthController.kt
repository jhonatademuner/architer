package com.architer.auth.presentation

import com.architer.auth.application.AuthService
import com.architer.auth.presentation.dto.AuthResponse
import com.architer.auth.presentation.dto.LoginRequest
import com.architer.auth.presentation.dto.RefreshRequest
import com.architer.user.presentation.dto.UserCreateRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(@RequestBody request: UserCreateRequest): AuthResponse {
        return authService.register(request)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): AuthResponse {
        return authService.login(request)
    }

    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") authHeader: String) {
        val accessToken = authHeader.removePrefix("Bearer ")
        authService.logout(accessToken)
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody request: RefreshRequest, @RequestHeader("Authorization") authHeader: String): AuthResponse {
        val accessToken = authHeader.removePrefix("Bearer ")
        return authService.refreshToken(request.refreshToken, accessToken)
    }
}