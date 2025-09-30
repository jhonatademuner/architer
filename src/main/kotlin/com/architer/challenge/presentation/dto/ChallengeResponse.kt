package com.architer.challenge.presentation.dto

import com.architer.challenge.domain.model.ChallengeCategory
import com.architer.challenge.domain.model.ChallengeDifficulty
import java.util.UUID

data class ChallengeResponse (
    var id: UUID?,
    var externalId: String,
    var icon: String? = null,
    var title: String,
    var overview: String,
    var description: String,
    var content: String,
    var category: ChallengeCategory,
    var difficulty: ChallengeDifficulty,
    var published: Boolean
)