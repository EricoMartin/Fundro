package com.basebox.fundro.domain.repository

import com.basebox.fundro.core.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun registerFcmToken(token: String): Flow<ApiResult<Unit>>
    suspend fun unregisterFcmToken(): Flow<ApiResult<Unit>>
}