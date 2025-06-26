package com.architer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties
class ArchiterApplication

fun main(args: Array<String>) {
	runApplication<ArchiterApplication>(*args)
}
