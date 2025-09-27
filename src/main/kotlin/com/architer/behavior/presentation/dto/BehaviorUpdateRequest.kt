package com.architer.behavior.presentation.dto

import java.util.UUID

data class BehaviorUpdateRequest(
    val id: UUID,
    val externalId: String,
    val icon: String? = null,
    val title: String,
    val overview: String,
    val description: String,
    val content: String,
)
