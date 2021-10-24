package com.example.webclient

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@RestController
class WebClientController(
    private val webClient: WebClient,
    private val helloService: SampleService
) {
    @RequestMapping("/mono-sync")
    fun monoSync(): String {
        val mono = webClient.get()
            .uri("http://localhost:8080/sleep-3000")
            .retrieve()
            .bodyToMono(String::class.java)

        logger.info {"END REQUEST"}
        logger.info {"RECV ${mono.block()}"}

        return "END-MONO-Sync ${LocalDateTime.now()}"
    }

    @RequestMapping("/mono-async-post")
    fun monoAsyncPost(): String {
        val mono = webClient.post()
            .uri("http://localhost:8080/sleep-3000")
            .bodyValue("Hello")
            .retrieve()
            .bodyToMono(String::class.java)

        var message ="MESSAGE_BEFORE"

        logger.info {"END REQUEST"}

        mono.subscribe {
            logger.info {"RECV $it"}
            helloService.hello(message)
        }

        message ="MESSAGE_AFTER"

        return "END-MONO-Async ${LocalDateTime.now()}"
    }

    @RequestMapping("/request-400")
    fun request400(): String {
        webClient.get()
            .uri("http://localhost:8080/throw-4xx")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        return "END-REQUEST-400 ${LocalDateTime.now()}"
    }

    @RequestMapping("/request-500")
    fun request500(): String {
        webClient.get()
            .uri("http://localhost:8080/throw-5xx")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        return "END-REQUEST-500 ${LocalDateTime.now()}"
    }

    @RequestMapping("/exchange")
    fun exchange(): String {
        val success = webClient.get()
            .uri("http://localhost:8080/sleep-3000")
            .exchangeToMono { response ->
                if (response.statusCode() == HttpStatus.OK) {
                    response.bodyToMono(String::class.java)
                } else {
                    Mono.error(Exception())
                }
            }

        val error = webClient.get()
            .uri("http://localhost:8080/throw-4xx")
            .exchangeToMono { response ->
                if (response.statusCode() == HttpStatus.OK) {
                    response.bodyToMono(String::class.java)
                } else {
                    Mono.error(Exception())
                }
            }

        logger.info { "$${success.block()}"}
        logger.info { "$${error.block()}"}

        return "END-EXCHANGE"
    }
}