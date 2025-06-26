package com.architer.dto.interview.message

import com.architer.domain.interview.InterviewRole

data class InterviewMessageCreateDTO(
    val role: InterviewRole,
    val text: String
)