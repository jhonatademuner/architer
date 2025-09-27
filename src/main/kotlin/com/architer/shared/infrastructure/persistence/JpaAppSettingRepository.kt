package com.architer.shared.infrastructure.persistence

import com.architer.shared.domain.model.AppSetting
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface JpaAppSettingRepository : JpaRepository<AppSetting, UUID> {
    fun findBySettingKey(settingKey: String): Optional<AppSetting>
}