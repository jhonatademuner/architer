package com.archter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ArchterApplication

fun main(args: Array<String>) {
	runApplication<ArchterApplication>(*args)
}
