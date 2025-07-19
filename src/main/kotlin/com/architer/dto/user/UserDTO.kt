package com.architer.dto.user

import java.util.UUID

data class UserDTO(
    var id: UUID?,
    var username: String,
    var email: String,
    var isAdmin: Boolean?,
    var isActive: Boolean?,
)
