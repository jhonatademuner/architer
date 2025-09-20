package com.architer.ai.domain.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OpenAIJsonSchema(
    var name: String,
    var strict: Boolean = true,
    var schema: SchemaData,
){
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class SchemaData(
        var type: String = "object",
        var required: List<String> = listOf(),
        @JsonProperty("additionalProperties")
        var additionalProperties: Boolean = false,
        var properties: Map<String, PropertyData> = mapOf(),
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class PropertyData(
        var type: String,
        var description: String,
        var enum: List<String>? = null,
        var items: SchemaData? = null,
    )
}