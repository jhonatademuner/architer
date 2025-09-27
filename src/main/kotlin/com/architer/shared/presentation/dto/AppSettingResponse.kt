package com.architer.shared.presentation.dto

import com.architer.shared.domain.model.enums.AppSettingValueType
import java.util.UUID

data class AppSettingResponse (
    val id: UUID?,
    val settingKey: String,
    val settingValue: String,
    val description: String,
    val valueType: AppSettingValueType
)