package com.architer.domain.behavior

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "assistant_behaviors")
@EntityListeners(AuditingEntityListener::class)
data class Behavior(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "overview", nullable = false)
    var overview: String,

    @Column(name = "content", nullable = false)
    var content: String,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    ){
    override fun toString(): String {
        return buildString {
            appendLine("Assistant Behavior Configuration")
            appendLine()
            appendLine("Name: $title")
            appendLine("Overview:")
            appendLine(overview)
            appendLine()
            appendLine("Content:")
            appendLine(content)
        }
    }
}