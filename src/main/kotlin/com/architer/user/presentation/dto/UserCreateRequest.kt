package com.architer.user.presentation.dto

data class UserCreateRequest(
    val name: String,
    var email: String,
    var password: String,
)
