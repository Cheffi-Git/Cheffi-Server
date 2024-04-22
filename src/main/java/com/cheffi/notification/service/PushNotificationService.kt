package com.cheffi.notification.service

import com.cheffi.notification.constant.DevicePlatform.*
import com.cheffi.notification.domain.PushNotification
import com.cheffi.notification.dto.MessageResponse
import com.cheffi.notification.repository.PushNotificationJpaRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class PushNotificationService(
    private val pushNotificationJpaRepository: PushNotificationJpaRepository,
    private val requestFailureService: RequestFailureService,
    private val fcmService: FcmService
) {


    fun sendAllAsync(ids: List<Long>) = runBlocking {
        val notifications = pushNotificationJpaRepository.findPushNotificationById(ids)
        val failedRequest = notifications.map {
            async { send(it) }
        }
        requestFailureService.logAll(failedRequest.awaitAll().filter { !it.successful })
    }

    private suspend fun send(pushNotification: PushNotification): MessageResponse =
        when (pushNotification.platform) {
            AOS -> fcmService.send(pushNotification)
            IOS -> fcmService.send(pushNotification)
            WEB -> fcmService.send(pushNotification)
        }

    fun sendAll(ids: List<Long>) = runBlocking {
        val notifications = pushNotificationJpaRepository.findPushNotificationById(ids)

        val failedRequest = notifications.map {
            send(it)
        }

        failedRequest.filter { !it.successful }
    }


}
