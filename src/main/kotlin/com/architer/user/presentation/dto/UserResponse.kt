package com.architer.user.presentation.dto

import java.util.UUID

data class UserResponse (
    var id: UUID,
    var name: String,
    var email: String,
)
