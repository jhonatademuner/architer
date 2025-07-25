package com.architer.config.filter

import com.architer.config.property.ApplicationProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Order(1)
@Component
class LoggingContextFilter (
    private val applicationProperties: ApplicationProperties
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val start = System.currentTimeMillis()
        val method = request.method
        val path = request.requestURI

        try {
            val requestId = request.getHeader("X-Request-ID") ?: UUID.randomUUID().toString()
            val traceId = request.getHeader("X-B3-TraceId") ?: UUID.randomUUID().toString()

            MDC.put("requestId", requestId)
            MDC.put("traceId", traceId)
            MDC.put("method", method)
            MDC.put("path", path)
            MDC.put("service", applicationProperties.name)
            MDC.put("logger", this::class.java.name)

            filterChain.doFilter(request, response)
        } finally {
            val duration = System.currentTimeMillis() - start
            MDC.put("duration", duration.toString())
            logger.info("$method $path - ${response.status} - ${duration}ms")
            MDC.clear()
        }
    }
}
