package com.architer.dto.challenge

import com.architer.domain.challenge.ChallengeCategory
import com.architer.domain.challenge.ChallengeDifficulty
import java.util.UUID

data class ChallengeUpdateDTO (
    val id: UUID,
    val title: String,
    val overview: String,
    val content: String,
    val category: ChallengeCategory,
    val difficulty: ChallengeDifficulty,
    val icon: String? = null
)