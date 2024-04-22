package com.cheffi.notification.dto

data class MessageResponse(
    val successful: Boolean,
    val data: Any
) {
    companion object {
        @JvmStatic
        fun fail(message: String = "Fail"): MessageResponse {
            return MessageResponse(data = message, successful = false)
        }

        @JvmStatic
        fun success(result: String): MessageResponse {
            return MessageResponse(data = result, successful = true)
        }
    }
}
