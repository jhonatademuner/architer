package com.architer.interview.domain.model

import com.architer.interview.domain.model.enums.InterviewFeedbackEvaluation
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "interview_detailed_feedbacks")
data class InterviewDetailedFeedback(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id", nullable = false, updatable = false)
    var feedback: InterviewFeedback? = null,

    @Column(name = "subject", nullable = false, updatable = false)
    var subject: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation", nullable = false, updatable = false)
    var evaluation: InterviewFeedbackEvaluation,

    @Column(name = "remarks", nullable = false, updatable = false)
    var remarks: String
)
