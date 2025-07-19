package com.architer.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.slf4j.MDC

@Configuration
class RestTemplateConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        val restTemplate = RestTemplate()
        restTemplate.interceptors.add { request, body, execution ->
            request.headers.add("X-Request-ID", MDC.get("requestId"))
            request.headers.add("X-B3-TraceId", MDC.get("traceId"))
            execution.execute(request, body)
        }
        return restTemplate
    }

}