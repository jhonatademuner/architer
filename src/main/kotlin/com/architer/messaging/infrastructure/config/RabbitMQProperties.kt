package com.architer.messaging.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "messaging.rabbitmq")
class RabbitMQProperties {
    lateinit var exchange: String
}