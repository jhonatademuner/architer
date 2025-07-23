package com.architer.dto.challenge

import java.util.UUID

data class SimplifiedChallengeDTO(
    var id: UUID?,
    var title: String,
    var overview: String,
    var category: String,
    var difficulty: String,
    var icon: String? = null
)
