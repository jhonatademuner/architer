package com.archter.dto.interview.message

import com.archter.domain.interview.InterviewRole

data class InterviewMessageCreateDTO (
    var role: InterviewRole,
    var content: String,
)