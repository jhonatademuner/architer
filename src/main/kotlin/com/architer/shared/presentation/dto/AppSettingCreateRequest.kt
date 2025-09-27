package com.architer.shared.presentation.dto

import com.architer.shared.domain.model.enums.AppSettingValueType

data class AppSettingCreateRequest(
    val settingKey: String,
    val settingValue: String,
    val description: String,
    val valueType: AppSettingValueType
)
