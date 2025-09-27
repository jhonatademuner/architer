package com.architer.shared.domain.repository

import com.architer.shared.domain.model.AppSetting
import java.util.Optional
import java.util.UUID

interface AppSettingRepository {
    fun save(entity: AppSetting): AppSetting
    fun findById(id: UUID): Optional<AppSetting>
    fun findBySettingKey(settingKey: String): Optional<AppSetting>
    fun deleteById(id: UUID)
}