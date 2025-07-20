package com.architer.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring.application")
class ApplicationProperties {
    lateinit var name: String
}