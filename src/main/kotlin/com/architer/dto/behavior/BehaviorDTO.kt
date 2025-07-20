package com.architer.dto.behavior

import java.time.LocalDateTime
import java.util.UUID

data class BehaviorDTO (

    var id: UUID?,
    var title: String,
    var overview: String,
    var content: String,
    var updatedAt: LocalDateTime?,
    var createdAt: LocalDateTime?

)