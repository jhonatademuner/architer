package com.architer.domain.user

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
@SQLDelete(sql = "UPDATE users SET active = false WHERE id = ?")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(name = "name", nullable = false, unique = true)
    var name: String,

    @Column(name = "email", nullable = false, unique = true)
    var email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "admin", nullable = false)
    var isAdmin: Boolean = false,

    @Column(name = "active", nullable = false)
    var isActive: Boolean = true,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null

)
