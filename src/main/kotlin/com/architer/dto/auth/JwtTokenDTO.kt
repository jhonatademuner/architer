package com.architer.dto.auth

data class JwtTokenDTO(
    val accessToken: String,
    val refreshToken: String
)
