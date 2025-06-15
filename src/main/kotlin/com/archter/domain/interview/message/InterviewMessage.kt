package com.archter.domain.interview.message

import com.archter.domain.interview.Interview
import com.archter.domain.interview.InterviewRole
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

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    var role: InterviewRole,

    @Column(name = "content", nullable = false)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview", nullable = false, updatable = false)
    var interview: Interview? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null

)