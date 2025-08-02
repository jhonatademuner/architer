package com.architer.dto.behavior

import java.util.UUID

data class BehaviorUpdateDTO(
    val id: UUID,
    val title: String,
    val overview: String,
    val content: String,
    val icon: String? = null
)