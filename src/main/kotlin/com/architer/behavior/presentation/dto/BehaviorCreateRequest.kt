package com.architer.behavior.presentation.dto

data class BehaviorCreateRequest (
    val icon: String? = null,
    val title: String,
    val overview: String,
    val description: String,
    val content: String,
)