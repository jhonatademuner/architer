package com.architer.behavior.presentation.dto

import java.util.UUID

data class BehaviorUpdateRequest(
    val icon: String? = null,
    val id: UUID,
    val title: String,
    val overview: String,
    val description: String,
    val content: String,
)
