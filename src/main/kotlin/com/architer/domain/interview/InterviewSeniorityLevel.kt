package com.architer.domain.interview

enum class InterviewSeniorityLevel(val displayName: String) {
    JUNIOR("Junior"),
    MID("Mid-level"),
    SENIOR("Senior"),
    ARCHITECT("Architect");

    companion object {
        fun fromDisplayName(displayName: String): InterviewSeniorityLevel {
            return InterviewSeniorityLevel.entries.firstOrNull { it.displayName == displayName }
                ?: throw IllegalArgumentException("Unknown seniority level: $displayName")
        }
    }
}