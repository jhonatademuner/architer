package com.architer.dto.challenge

import java.util.UUID

data class ChallengeUpdateDTO (
    val id: UUID,
    val title: String,
    val overview: String,
    val content: String,
)