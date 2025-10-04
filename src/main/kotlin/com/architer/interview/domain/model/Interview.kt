package com.architer.interview.domain.model

import com.architer.behavior.domain.model.Behavior
import com.architer.challenge.domain.model.Challenge
import com.architer.interview.domain.model.enums.InterviewSeniority
import com.architer.interview.domain.model.enums.InterviewStatus
import jakarta.persistence.*
import lombok.Builder
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "interviews")
@EntityListeners(AuditingEntityListener::class)
data class Interview (

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: InterviewStatus = InterviewStatus.ON_GOING,

    @Column(name = "duration", nullable = true)
    var duration: Int? = null, // in seconds

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "behavior_id", nullable = false, updatable = false)
    var behavior: Behavior,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false, updatable = false)
    var challenge: Challenge,

    @Column(name = "seniority", nullable = false)
    @Enumerated(EnumType.STRING)
    var seniority: InterviewSeniority = InterviewSeniority.JUNIOR,

    @Column(name = "user_id", nullable = false, updatable = false)
    var userId: UUID,

    @LastModifiedDate
    @Column(name = "last_modified_at", nullable = false)
    var lastModifiedAt: LocalDateTime? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "interview", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    var messages: MutableList<InterviewMessage> = mutableListOf(),

    @OneToOne(mappedBy = "interview", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var feedback: InterviewFeedback? = null,
) {
    fun addMessage(message: InterviewMessage) {
        message.interview = this
        messages.add(message)
    }

    fun addFeedback(interviewFeedback: InterviewFeedback) {
        interviewFeedback.interview = this
        feedback = interviewFeedback
    }
}