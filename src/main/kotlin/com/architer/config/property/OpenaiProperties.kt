package com.architer.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "clients.openai")
class OpenaiProperties {
    lateinit var apiUrl: String
    lateinit var apiKey: String
}