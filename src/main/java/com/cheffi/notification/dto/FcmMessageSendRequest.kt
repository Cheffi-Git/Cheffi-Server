package com.cheffi.notification.dto

import com.cheffi.notification.domain.PushNotification

data class FcmMessageSendRequest(
    val message: Message
) {

    data class Message(
        val notification: Notification,
        val token: String
    )

    data class Notification(
        val title: String,
        val body: String,
        val image: String? = null
    )


    companion object {
        @JvmStatic
        fun of(pn: PushNotification): FcmMessageSendRequest {
            val message = Message(
                notification = Notification(
                    title = pn.title,
                    body = pn.body
                ),
                token = pn.token
            )
            return FcmMessageSendRequest(message)
        }
    }

}
