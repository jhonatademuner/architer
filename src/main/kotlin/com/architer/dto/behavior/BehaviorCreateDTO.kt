package com.architer.dto.behavior

data class BehaviorCreateDTO(
    val title: String,
    val overview: String,
    val content: String,
    val icon: String? = null
)
