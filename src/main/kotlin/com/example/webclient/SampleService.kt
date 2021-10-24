package com.example.webclient

import mu.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class SampleService {
    fun hello(message: String) {
        logger.info { "Hello $message" }
    }
}