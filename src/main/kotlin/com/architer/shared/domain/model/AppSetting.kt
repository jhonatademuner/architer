package com.architer.shared.domain.model

import com.architer.shared.domain.model.enums.AppSettingValueType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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
@Table(name = "app_settings")
@EntityListeners(AuditingEntityListener::class)
data class AppSetting(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(name = "setting_key", nullable = false, unique = true)
    var settingKey: String,

    @Column(name = "setting_value")
    var settingValue: String,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "value_type", nullable = false)
    @Enumerated(EnumType.STRING)
    var valueType: AppSettingValueType,

    @LastModifiedDate
    @Column(name = "last_modified_at", nullable = false)
    var lastModifiedAt: LocalDateTime? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,
)
