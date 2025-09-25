package com.architer.challenge.domain.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "challenges")
@EntityListeners(AuditingEntityListener::class)
data class Challenge(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(name = "external_id", nullable = false)
    var externalId: String,

    @Column(name = "icon", nullable = true)
    var icon: String? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "overview", nullable = false)
    var overview: String,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    var category: ChallengeCategory,

    @Column(name = "difficulty", nullable = false)
    @Enumerated(EnumType.STRING)
    var difficulty: ChallengeDifficulty,

    @LastModifiedDate
    @Column(name = "last_modified_at", nullable = false)
    var lastModifiedAt: LocalDateTime? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    )
