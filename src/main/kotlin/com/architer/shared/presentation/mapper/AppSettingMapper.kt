package com.architer.shared.presentation.mapper

import com.architer.shared.domain.model.AppSetting
import com.architer.shared.presentation.dto.AppSettingCreateRequest
import com.architer.shared.presentation.dto.AppSettingResponse
import com.architer.shared.presentation.dto.AppSettingUpdateRequest
import org.springframework.stereotype.Component

@Component
class AppSettingMapper {
    fun toEntity(response: AppSettingResponse): AppSetting {
        return AppSetting(
            id = response.id,
            settingKey = response.settingKey,
            settingValue = response.settingValue,
            description = response.description,
            valueType = response.valueType
        )
    }

    fun toEntity(request: AppSettingCreateRequest): AppSetting {
        return AppSetting(
            settingKey = request.settingKey,
            settingValue = request.settingValue,
            description = request.description,
            valueType = request.valueType
        )
    }

    fun toResponse(entity: AppSetting): AppSettingResponse {
        return AppSettingResponse(
            id = entity.id,
            settingKey = entity.settingKey,
            settingValue = entity.settingValue,
            description = entity.description,
            valueType = entity.valueType
        )
    }

    fun updateEntity(request: AppSettingUpdateRequest, entity: AppSetting): AppSetting {
        return entity.apply {
            settingKey = request.settingKey
            settingValue = request.settingValue
            description = request.description
            valueType = request.valueType
        }
    }
}