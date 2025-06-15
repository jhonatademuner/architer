package com.archter.dto.challenge

import java.time.LocalDateTime
import java.util.UUID

data class ChallengeDTO(

    var id: UUID?,
    var title: String,
    var overview: String,
    var content: String,
    var updatedAt: LocalDateTime?,
    var createdAt: LocalDateTime?

)
