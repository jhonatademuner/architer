package com.architer.dto.assistant.behavior

import java.time.LocalDateTime
import java.util.UUID

data class AssistantBehaviorDTO (

    var id: UUID?,
    var title: String,
    var overview: String,
    var content: String,
    var updatedAt: LocalDateTime?,
    var createdAt: LocalDateTime?

)