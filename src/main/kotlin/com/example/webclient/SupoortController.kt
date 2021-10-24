package com.example.webclient

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {}

@RestController
class SupoortController {
    @RequestMapping("/sleep-3000")
    fun sleep3000(@RequestHeader("HEADER", required = false) header: String?,
                  @CookieValue("COOKIE", required = false) cookie: String?,
                  @RequestBody body: String?): String {
        logger.info { "BEFORE SLEEP" }
        Thread.sleep(3000)

        return "SUCCESS $header $cookie $body"
    }

    @RequestMapping("/throw-4xx")
    fun throw4xx(): ResponseEntity<String> {
        return ResponseEntity.badRequest().body("")
    }

    @RequestMapping("/throw-5xx")
    fun throw5xx(): ResponseEntity<String> {
        return ResponseEntity.internalServerError().body("")
    }
}