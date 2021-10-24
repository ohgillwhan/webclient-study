package com.example.webclient

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .defaultCookie("COOKIE", "default-cookie")
            .defaultHeader("HEADER", "default-header")
            .build()
    }
}