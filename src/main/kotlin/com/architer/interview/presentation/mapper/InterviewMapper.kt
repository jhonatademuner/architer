package com.architer.interview.presentation.mapper

import com.architer.behavior.domain.model.Behavior
import com.architer.behavior.presentation.mapper.BehaviorMapper
import com.architer.challenge.domain.model.Challenge
import com.architer.challenge.presentation.mapper.ChallengeMapper
import com.architer.interview.domain.model.Interview
import com.architer.interview.domain.model.InterviewMessage
import com.architer.interview.domain.model.enums.InterviewSeniority
import com.architer.interview.presentation.dto.InterviewResponse
import com.architer.interview.presentation.dto.InterviewSimplifiedResponse
import com.architer.user.domain.model.User
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class InterviewMapper(
    private val interviewFeedbackMapper: InterviewFeedbackMapper,
    private val behaviorMapper: BehaviorMapper,
    private val challengeMapper: ChallengeMapper
) {

    fun toResponse(entity: Interview): InterviewResponse {
        return InterviewResponse(
            id = entity.id,
            title = entity.title,
            duration = entity.duration,
            feedback = interviewFeedbackMapper.toResponse(entity.feedback),
            seniority = entity.seniority,
            behavior = behaviorMapper.toResponse(entity.behavior),
            challenge = challengeMapper.toResponse(entity.challenge),
            createdAt = entity.createdAt
        )
    }

    fun toSimplifiedResponse(entity: Interview): InterviewSimplifiedResponse {
        return InterviewSimplifiedResponse(
            id = entity.id,
            title = entity.title,
            duration = entity.duration,
            behaviorTitle = entity.behavior.title,
            challengeTitle = entity.challenge.title,
            seniority = entity.seniority,
            status = entity.status,
            createdAt = entity.createdAt
        )
    }

    fun toEntity(behavior: Behavior, challenge: Challenge, seniority: InterviewSeniority, messages: List<InterviewMessage>, userId: UUID): Interview{
        val entity = Interview(
            title = challenge.title,
            behavior = behavior,
            challenge = challenge,
            seniority = seniority,
            userId = userId
        )

        messages.forEach { entity.addMessage(it) }
        return entity
    }
}