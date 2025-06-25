package com.archter.client.assistant

import com.archter.config.property.OpenaiProperties
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class AssistantClient(
    private val restTemplate: RestTemplate,
    private val openaiProperties: OpenaiProperties,
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

    fun <T> get(
        endpoint: String,
        params: Map<String, String> = emptyMap(),
        responseType: Class<T>,
    ): ResponseEntity<T> {
        val url = buildUri(endpoint, params)
        val entity = HttpEntity<String>(createHeaders())
        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType)
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

    fun <T> put(
        endpoint: String,
        request: Any,
        params: Map<String, String> = emptyMap(),
        responseType: Class<T>,
    ): ResponseEntity<T> {
        val url = buildUri(endpoint, params)
        val entity = HttpEntity(request, createHeaders())
        return restTemplate.exchange(url, HttpMethod.PUT, entity, responseType)
    }

    fun <T> delete(
        endpoint: String,
        params: Map<String, String> = emptyMap(),
        responseType: Class<T>,
    ): ResponseEntity<T> {
        val url = buildUri(endpoint, params)
        val entity = HttpEntity<String>(createHeaders())
        return restTemplate.exchange(url, HttpMethod.DELETE, entity, responseType)
    }

}