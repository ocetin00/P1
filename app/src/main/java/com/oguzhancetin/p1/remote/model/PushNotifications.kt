package com.oguzhancetin.p1.remote.model

data class PushNotification(
    val data: NotificationData,
    val to: String,
)

data class NotificationData(
    val price: String?,
    val pay_type: String?,
    val source: String?

)