package com.oguzhancetin.p1.remote

import com.oguzhancetin.p1.util.Constants.Companion.CONTENT_TYPE
import com.oguzhancetin.p1.util.Constants.Companion.SERVER_KEY
import com.oguzhancetin.p1.remote.model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FirebaseServiceApi {
    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}