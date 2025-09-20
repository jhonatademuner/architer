package com.architer.interview.domain.model.enums

enum class InterviewDetailedFeedbackSubject(val displayName: String) {
    PROBLEM_UNDERSTANDING("Problem Understanding"),
    PROBLEM_SOLUTION("Problem Solution"),
    CODE_QUALITY("Code Quality"),
    ALGORITHM("Algorithm"),
    SYSTEM_DESIGN("System Design"),
    IMPROVEMENT("Improvement");

    companion object {
        fun fromDisplayName(displayName: String): InterviewDetailedFeedbackSubject? {
            return values().firstOrNull { it.displayName == displayName }
        }

        fun getAllNames(): List<String> {
            return values().map { it.name }
        }
    }
}