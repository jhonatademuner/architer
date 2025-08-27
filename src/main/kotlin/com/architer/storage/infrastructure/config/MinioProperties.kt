package com.architer.storage.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "clients.minio")
class MinioProperties {
    lateinit var baseUrl: String
    lateinit var accessKey: String
    lateinit var secretKey: String
    lateinit var bucket: String
}