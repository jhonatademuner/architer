package com.architer.interview.domain.model.enums

enum class InterviewFeedbackEvaluation(val score: Int) {
    POOR(1),                   // Major misunderstandings, incomplete answers
    NEEDS_IMPROVEMENT(2),      // Significant gaps in reasoning or coverage
    SATISFACTORY(3),           // Adequate, but room for improvement
    GOOD(4),                   // Solid performance, minor gaps
    EXCELLENT(5);              // Outstanding, very few or no gaps

    // Use to get all the evaluation in a list
    companion object {
        fun getAllNames(): List<String> {
            return values().map { it.name }
        }
    }
}
