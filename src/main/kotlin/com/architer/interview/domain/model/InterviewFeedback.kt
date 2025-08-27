package com.architer.interview.domain.model

import com.architer.interview.domain.model.enums.InterviewFeedbackEvaluation
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "interview_feedbacks")
@EntityListeners(AuditingEntityListener::class)
data class InterviewFeedback (

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false, updatable = false, unique = true)
    var interview: Interview? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "overall_evaluation", nullable = false, updatable = false)
    var overallEvaluation: InterviewFeedbackEvaluation,

    @Column(name = "overall_remarks", nullable = false, updatable = false)
    var overallRemarks: String,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "feedback", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    var detailedFeedbacks: MutableList<InterviewDetailedFeedback> = mutableListOf(),

    )