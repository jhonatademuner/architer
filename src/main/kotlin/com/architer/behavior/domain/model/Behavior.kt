package com.architer.behavior.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "behaviors")
@EntityListeners(AuditingEntityListener::class)
data class Behavior(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(name = "icon", nullable = true)
    var icon: String? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "overview", nullable = false)
    var overview: String,

    @Column(name = "description", nullable = true)
    var description: String,

    @Column(name = "content", nullable = false)
    var content: String,

    @LastModifiedDate
    @Column(name = "last_modified_at", nullable = false)
    var lastModifiedAt: LocalDateTime? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    )
