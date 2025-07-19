package com.architer.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "security.jwt")
class JwtProperties {
    lateinit var secretKey: String
    lateinit var issuer: String
    lateinit var audience: String
    var accessExpirationMs: Long = 15 * 60 * 1000 // 15 minutes
    var refreshExpirationMs: Long = 7 * 24 * 60 * 60 * 1000 // 7 days
}
