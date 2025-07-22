package com.architer.dto.challenge

import java.util.UUID

data class ChallengeDTO(

    var id: UUID?,
    var title: String,
    var overview: String,
    var content: String,
    var category: String,
    var difficulty: String,
    var icon: String? = null

)
