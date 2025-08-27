package com.architer.ai.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "clients.openai")
class OpenAiProperties {
    lateinit var apiUrl: String
    lateinit var apiKey: String
}