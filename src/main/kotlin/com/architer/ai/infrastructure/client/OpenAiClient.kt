package com.architer.ai.infrastructure.client

import com.architer.ai.infrastructure.config.OpenAiProperties
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class OpenAiClient(
    private val restTemplate: RestTemplate,
    private val openaiProperties: OpenAiProperties,
) {
    private fun createHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Bearer ${openaiProperties.apiKey}")
        }
    }

    private fun buildUri(endpoint: String, params: Map<String, String>): URI {
        val baseUri = URI.create("${openaiProperties.apiUrl}/$endpoint")
        val uriBuilder = UriComponentsBuilder.fromUri(baseUri)
        params.forEach { (key, value) ->
            uriBuilder.queryParam(key, value)
        }
        return uriBuilder.build(true).toUri()
    }

    fun <T> post(
        endpoint: String,
        request: Any,
        params: Map<String, String> = emptyMap(),
        responseType: Class<T>,
    ): ResponseEntity<T> {
        val url = buildUri(endpoint, params)
        val entity = HttpEntity(request, createHeaders())
        return restTemplate.exchange(url, HttpMethod.POST, entity, responseType)
    }
}