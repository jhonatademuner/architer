package com.architer.challenge.presentation.dto

import com.architer.challenge.domain.model.ChallengeCategory
import com.architer.challenge.domain.model.ChallengeDifficulty

data class ChallengeCreateRequest(
    val icon: String? = null,
    val title: String,
    val overview: String,
    val description: String,
    val content: String,
    val category: ChallengeCategory,
    val difficulty: ChallengeDifficulty,
)