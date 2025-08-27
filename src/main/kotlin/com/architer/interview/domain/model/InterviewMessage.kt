package com.architer.interview.domain.model

import com.architer.interview.domain.model.enums.InterviewRole
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "interview_messages")
@EntityListeners(AuditingEntityListener::class)
data class InterviewMessage(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false, updatable = false)
    var interview: Interview? = null,

    @Column(name = "role", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    var role: InterviewRole,

    @Column(name = "content", nullable = false, updatable = false)
    var content: String,

    @CreatedDate
    @Column(name = "sent_at", nullable = false, updatable = false)
    var sentAt: LocalDateTime? = null

    )