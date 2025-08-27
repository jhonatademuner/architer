package com.architer.auth.presentation.dto

data class AuthResponse (
    val accessToken: String,
    val refreshToken: String,
)