package com.architer.dto.behavior

import java.util.UUID

data class SimplifiedBehaviorDTO(
    var id: UUID?,
    var title: String,
    var overview: String,
    var icon: String? = null
)
