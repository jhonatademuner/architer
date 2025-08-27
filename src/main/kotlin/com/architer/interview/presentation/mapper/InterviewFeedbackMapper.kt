package com.architer.interview.presentation.mapper

import com.architer.interview.domain.model.InterviewDetailedFeedback
import com.architer.interview.domain.model.InterviewFeedback
import com.architer.interview.presentation.dto.InterviewDetailedFeedbackResponse
import com.architer.interview.presentation.dto.InterviewFeedbackResponse
import org.springframework.stereotype.Component

@Component
class InterviewFeedbackMapper {

    fun toResponse(entity: InterviewFeedback?): InterviewFeedbackResponse? {
        if (entity == null) return null
        return InterviewFeedbackResponse(
            id = entity.id,
            overallEvaluation = entity.overallEvaluation,
            overallRemarks = entity.overallRemarks,
            detailedFeedbacks = entity.detailedFeedbacks.mapNotNull { DetailedFeedbackMapper().toResponse(it) }
        )
    }

    class DetailedFeedbackMapper {
        fun toResponse(entity: InterviewDetailedFeedback?): InterviewDetailedFeedbackResponse? {
            if (entity == null) return null
            return InterviewDetailedFeedbackResponse(
                id = entity.id,
                subject = entity.subject,
                evaluation = entity.evaluation,
                remarks = entity.remarks
            )
        }
    }
}