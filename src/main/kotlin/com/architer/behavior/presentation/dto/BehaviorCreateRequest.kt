package com.architer.behavior.presentation.dto

data class BehaviorCreateRequest (
    val externalId: String,
    val icon: String? = null,
    val title: String,
    val overview: String,
    val description: String,
    val content: String,
    val published: Boolean = true
)