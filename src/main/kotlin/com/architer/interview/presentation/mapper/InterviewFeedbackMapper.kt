package com.architer.interview.presentation.mapper

import com.architer.ai.domain.model.message.AssistantMessage
import com.architer.interview.domain.model.Interview
import com.architer.interview.domain.model.InterviewDetailedFeedback
import com.architer.interview.domain.model.InterviewFeedback
import com.architer.interview.domain.model.enums.InterviewDetailedFeedbackSubject
import com.architer.interview.domain.model.enums.InterviewFeedbackEvaluation
import com.architer.interview.presentation.dto.InterviewDetailedFeedbackResponse
import com.architer.interview.presentation.dto.InterviewFeedbackResponse
import com.fasterxml.jackson.databind.ObjectMapper
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

    fun toEntity(assistantMessage: AssistantMessage, interview: Interview): InterviewFeedback {
        val mapper = ObjectMapper()
        val jsonNode = mapper.readTree(assistantMessage.content)
        
        val feedback = InterviewFeedback(
            interview = interview,
            overallEvaluation = InterviewFeedbackEvaluation.valueOf(jsonNode.get("overallEvaluation").asText()),
            overallRemarks = jsonNode.get("overallRemarks").asText()
        )
        
        jsonNode.get("detailedFeedbacks")?.forEach { detailNode ->
            val detailedFeedback = InterviewDetailedFeedback(
                subject = InterviewDetailedFeedbackSubject.valueOf(detailNode.get("subject").asText()),
                evaluation = InterviewFeedbackEvaluation.valueOf(detailNode.get("evaluation").asText()),
                remarks = detailNode.get("remarks").asText()
            )
            detailedFeedback.feedback = feedback
            feedback.detailedFeedbacks.add(detailedFeedback)
        }
        
        return feedback
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