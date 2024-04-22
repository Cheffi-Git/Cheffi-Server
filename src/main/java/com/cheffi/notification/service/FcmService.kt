package com.cheffi.notification.service

import com.cheffi.notification.domain.PushNotification
import com.cheffi.notification.dto.FcmMessageSendRequest
import com.cheffi.notification.dto.MessageResponse
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.io.FileInputStream
import java.time.Duration

@Service
class FcmService(
    private val webClient: WebClient,
) {
    @Value("\${fcm.config.file-path}")
    private lateinit var fcmConfigFilePath: String
    private val log = LoggerFactory.getLogger(FcmService::class.java)

    companion object {
        const val FCM_SEND_URL = "https://fcm.googleapis.com/v1/projects/cheffi/messages:send"
        const val FCM_ACCESS_TOKEN_URL = "https://www.googleapis.com/auth/firebase.messaging"
    }


    suspend fun send(
        pushNotification: PushNotification,
        maxRetries: Int = 3,
        delayDuration: Duration = Duration.ofSeconds(1L)
    ): MessageResponse {
        var message: String = "Default Message"
        var attempt = 0
        while (attempt < maxRetries) {
            try {
                val result = webClient.post()
                    .uri(FCM_SEND_URL)
                    .bodyValue(FcmMessageSendRequest.of(pushNotification))
                    .header("Authorization", "Bearer ${getAccessToken()}")
                    .header("Content-Type", "application/json; UTF-8")
                    .retrieve().awaitBody<String>()
                return MessageResponse.success(result)
            } catch (e: Exception) {
                log.info(e.message)
                message = e.message.toString()
                delay(delayDuration.toMillis() * (attempt + 1))
                attempt++
            }
        }

        return MessageResponse.fail(message)
    }

    fun getAccessToken(): String {
        val googleCredentials =
            GoogleCredentials.fromStream(FileInputStream(fcmConfigFilePath))
                .createScoped(FCM_ACCESS_TOKEN_URL)
        googleCredentials.refreshIfExpired()
        return googleCredentials.accessToken.tokenValue
    }

}
