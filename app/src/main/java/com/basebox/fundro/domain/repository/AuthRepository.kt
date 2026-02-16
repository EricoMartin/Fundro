package com.basebox.fundro.domain.repository

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(
        username: String,
        fullName: String,
        email: String,
        password: String,
        phoneNumber: String,
        role: String,
        bvn: String,
        bankAccountNumber: String,
        bankCode: String,
        bankName: String,
        accountHolderName: String,
    ): Flow<ApiResult<User>>

    suspend fun login(
        email: String,
        password: String
    ): Flow<ApiResult<User>>

    suspend fun getCurrentUser(): Flow<ApiResult<User>>

    suspend fun logout(): Flow<ApiResult<Unit>>

    fun isLoggedIn(): Boolean

    fun isOnboardingCompleted(): Boolean
}