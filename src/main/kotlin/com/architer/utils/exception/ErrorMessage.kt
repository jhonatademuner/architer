package com.architer.utils.exception

import java.time.LocalDateTime

data class ErrorMessage(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: String?,
    val path: String
)
