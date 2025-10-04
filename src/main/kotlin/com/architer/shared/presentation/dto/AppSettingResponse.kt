package com.architer.shared.presentation.dto

import com.architer.shared.domain.model.enums.AppSettingValueType
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class AppSettingResponse (
    val id: UUID?,
    val settingKey: String,
    @JsonProperty("settingValue")
    val settingValue: String,
    val description: String,
    val valueType: AppSettingValueType
){
    @JsonIgnore
    fun getValueAsString() : String = settingValue
    @JsonIgnore
    fun getValueAsInt() : Int = settingValue.toInt()
    @JsonIgnore
    fun getValueAsBoolean() : Boolean = settingValue.toBoolean()
    @JsonIgnore
    fun getValueAsFloat() : Float = settingValue.toFloat()
}