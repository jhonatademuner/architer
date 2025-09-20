package com.architer.ai.domain.model

import com.architer.ai.domain.model.message.AssistantBaseMessage
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ChatCompletionRequest(
    var model: String,
    var messages: List<AssistantBaseMessage>,
    @JsonProperty("response_format")
    var responseFormat: ResponseFormat? = null,
) {
    data class ResponseFormat(
        var type: String = "json_schema",
        @JsonProperty("json_schema")
        var jsonSchema: OpenAIJsonSchema
    )

    fun setResponseFormat(schema: OpenAIJsonSchema?){
        if (schema == null) return
        this.responseFormat = ResponseFormat(jsonSchema = schema)
    }
}
