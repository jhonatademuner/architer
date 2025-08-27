package com.architer.interview.presentation.dto

import com.architer.interview.domain.model.enums.InterviewRole

data class InterviewMessageSendRequest (
    val role: InterviewRole,
    val text: String
)