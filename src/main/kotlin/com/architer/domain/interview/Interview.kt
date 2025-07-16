package com.architer.domain.interview

import com.architer.domain.behavior.Behavior
import com.architer.domain.challenge.Challenge
import com.architer.domain.interview.message.InterviewMessage
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
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

    @Column(name = "timeSpent", nullable = true)
    var timeSpent: Int? = null, // in seconds

    @Column(name = "feedback", nullable = true)
    var feedback: String? = null,

    @OneToMany(mappedBy = "interview", cascade = [CascadeType.ALL], orphanRemoval = true)
    @Builder.Default
    var messages: MutableList<InterviewMessage> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "behavior", nullable = false, updatable = false)
    var behavior: Behavior? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge", nullable = false, updatable = false)
    var challenge: Challenge? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null

) {

    fun addMessage(message: InterviewMessage) {
        message.interview = this
        messages.add(message)
    }

}