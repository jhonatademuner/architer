package com.architer.controller.auth

import com.architer.dto.auth.JwtTokenDTO
import com.architer.dto.auth.RefreshTokenRequestDTO
import com.architer.dto.auth.UserLoginDTO
import com.architer.dto.auth.UserRegisterDTO
import com.architer.service.auth.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/v1/auth/register")
    fun register(@RequestBody dto: UserRegisterDTO): ResponseEntity<Unit> {
        authService.register(dto)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/v1/auth/login")
    fun login(@RequestBody dto: UserLoginDTO): ResponseEntity<JwtTokenDTO> {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(dto.login, dto.password))
    }

    @PostMapping("/v1/auth/logout")
    fun logout(@RequestHeader("Authorization") authHeader: String) {
        val token = authHeader.removePrefix("Bearer ")
        authService.revokeToken(token)
    }

    @PostMapping("/v1/auth/refresh")
    fun refreshToken(@RequestBody request: RefreshTokenRequestDTO, @RequestHeader("Authorization") authHeader: String): ResponseEntity<JwtTokenDTO> {
        val accessToken = authHeader.removePrefix("Bearer ")
        val tokens = authService.refreshToken(request.refreshToken, accessToken)
        return ResponseEntity.ok(tokens)
    }

}