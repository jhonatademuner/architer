package com.architer.behavior.presentation.dto

import java.util.UUID

data class BehaviorResponse(
    var id: UUID?,
    var icon: String? = null,
    var title: String,
    var overview: String,
    var description: String,
    var content: String,
)
