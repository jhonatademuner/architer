package com.architer.interview.infrastructure.ai

import com.architer.ai.domain.model.OpenAIJsonSchema
import com.architer.interview.domain.model.enums.InterviewDetailedFeedbackSubject
import com.architer.interview.domain.model.enums.InterviewFeedbackEvaluation

class InterviewFeedbackResponseFormat {
    companion object {
        fun getOpenAISchema(): OpenAIJsonSchema {
            val detailedFeedbackProperties = mapOf(
                "subject" to OpenAIJsonSchema.PropertyData(
                    type = "string",
                    description = "The specific aspect of the interview being evaluated",
                    enum = InterviewDetailedFeedbackSubject.getAllNames()
                ),
                "evaluation" to OpenAIJsonSchema.PropertyData(
                    type = "string",
                    description = "The performance rating for this subject area",
                    enum = InterviewFeedbackEvaluation.getAllNames()
                ),
                "remarks" to OpenAIJsonSchema.PropertyData(
                    type = "string",
                    description = "Specific feedback and suggestions for improvement in this area"
                )
            )

            val properties = mapOf(
                "overallEvaluation" to OpenAIJsonSchema.PropertyData(
                    type = "string",
                    description = "Overall assessment of the candidate's interview performance"
                ),
                "overallRemarks" to OpenAIJsonSchema.PropertyData(
                    type = "string",
                    description = "General feedback summary and key recommendations for the candidate"
                ),
                "detailedFeedbacks" to OpenAIJsonSchema.PropertyData(
                    type = "array",
                    description = "Detailed feedback broken down by specific evaluation subjects",
                    items = OpenAIJsonSchema.SchemaData(
                        type = "object",
                        properties = detailedFeedbackProperties,
                        required = listOf("subject", "evaluation", "remarks"),
                        additionalProperties = false
                    )
                )
            )

            val schemaData = OpenAIJsonSchema.SchemaData(
                type = "object",
                properties = properties,
                required = listOf("overallEvaluation", "overallRemarks", "detailedFeedbacks"),
                additionalProperties = false
            )

            val dataFormat = OpenAIJsonSchema(
                name = "interviewFeedback",
                schema = schemaData
            )
            return dataFormat
        }


    }
}