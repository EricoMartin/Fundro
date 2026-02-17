package com.basebox.fundro.data.repository
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.data.remote.api.NotificationApi
import com.basebox.fundro.data.remote.dto.request.FcmTokenRequest
import com.basebox.fundro.domain.repository.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApi: NotificationApi
) : NotificationRepository {

    override suspend fun registerFcmToken(token: String): Flow<ApiResult<Unit>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = notificationApi.registerFcmToken(
                FcmTokenRequest(token = token)
            )

            if (response.isSuccessful) {
                emit(ApiResult.Success(Unit))
                Timber.d("FCM token registered successfully")
            } else {
                emit(ApiResult.Error("Failed to register FCM token: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
            Timber.e(e, "FCM token registration error")
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun unregisterFcmToken(): Flow<ApiResult<Unit>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = notificationApi.unregisterFcmToken()

            if (response.isSuccessful) {
                emit(ApiResult.Success(Unit))
                Timber.d("FCM token unregistered successfully")
            } else {
                emit(ApiResult.Error("Failed to unregister FCM token"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)
}