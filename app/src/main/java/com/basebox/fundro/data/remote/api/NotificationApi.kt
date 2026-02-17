package com.basebox.fundro.data.remote.api
import com.basebox.fundro.data.remote.dto.request.FcmTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface NotificationApi {

    @POST("notifications/fcm-token")
    suspend fun registerFcmToken(
        @Body request: FcmTokenRequest
    ): Response<Unit>

    @DELETE("notifications/fcm-token")
    suspend fun unregisterFcmToken(): Response<Unit>
}