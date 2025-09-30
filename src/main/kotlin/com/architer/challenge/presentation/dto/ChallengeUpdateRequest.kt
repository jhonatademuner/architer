package com.architer.challenge.presentation.dto

import com.architer.challenge.domain.model.ChallengeCategory
import com.architer.challenge.domain.model.ChallengeDifficulty
import java.util.UUID

data class ChallengeUpdateRequest(
    val id: UUID,
    val externalId: String,
    val icon: String? = null,
    val title: String,
    val overview: String,
    val description: String,
    val content: String,
    val category: ChallengeCategory,
    val difficulty: ChallengeDifficulty,
    val published: Boolean
)