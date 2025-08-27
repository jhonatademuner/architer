package com.architer.auth.presentation.dto

data class LoginRequest(
    val email: String,
    val password: String,
)
