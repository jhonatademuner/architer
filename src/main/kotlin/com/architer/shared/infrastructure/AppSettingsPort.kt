package com.architer.shared.infrastructure

import com.architer.shared.presentation.dto.AppSettingCreateRequest
import com.architer.shared.presentation.dto.AppSettingResponse
import com.architer.shared.presentation.dto.AppSettingUpdateRequest
import java.util.UUID

interface AppSettingsPort {
    fun create(request: AppSettingCreateRequest): AppSettingResponse
    fun findById(id: UUID): AppSettingResponse
    fun findBySettingKey(settingKey: String): AppSettingResponse
    fun update(request: AppSettingUpdateRequest): AppSettingResponse
    fun delete(id: UUID)
}