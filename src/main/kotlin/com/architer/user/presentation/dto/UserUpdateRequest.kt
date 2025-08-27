package com.architer.user.presentation.dto

import java.util.UUID

data class UserUpdateRequest (
    val id: UUID,
    val name: String,
    val email: String,
    var password: String,
)