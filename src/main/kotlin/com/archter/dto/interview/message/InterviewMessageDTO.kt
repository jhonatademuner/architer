package com.archter.dto.interview.message

import com.archter.domain.interview.InterviewRole
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class InterviewMessageDTO(
    var id: UUID? = null,
    override var role: InterviewRole,
    var content: String,
    var interviewId: UUID? = null,
    var createdAt: LocalDateTime? = null
) : BaseMessageDTO