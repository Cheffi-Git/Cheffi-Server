package com.cheffi.review.service

import com.cheffi.event.event.ReviewReadEvent
import com.cheffi.notification.dto.MessageResponse
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.lang.Thread.sleep

@Service
class ViewCountingService(
    private val webClient: WebClient,
    private val rabbitTemplate: RabbitTemplate,
) {

    companion object {
        private const val VIEW_COUNT_REQUEST_URL = "http://localhost:8081/reviews/count"
        private const val EXCHANGE_NAME = "viewcount.direct";
        private const val ROUTING_KEY = "viewcount.routing.#"
    }

    fun requestViewCount(
        event: ReviewReadEvent,
    ): MessageResponse {
        var message: String = "Default Message"
        var attempt = 0

        while (attempt < 3) {
            try {
                val result = webClient.post()
                    .uri(VIEW_COUNT_REQUEST_URL)
                    .bodyValue(event)
                    .retrieve()
                    .bodyToMono<String>().block()
                return result?.let { MessageResponse.success(it) }
                    ?: throw RuntimeException("응답 값이 Null 입니다.")
            } catch (e: Exception) {
                message = e.message.toString()
                sleep(1000L * (attempt + 1))
                attempt++
            }
        }

        return MessageResponse.fail(message)
    }

    fun sendMessage(
        event: ReviewReadEvent,
    ): MessageResponse {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, event)
        return MessageResponse.success("Success")
    }


}
