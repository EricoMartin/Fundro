package com.basebox.fundro.data.repository

import android.content.Context
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.core.security.SecureStorage
import com.basebox.fundro.data.local.dao.UserDao
import com.basebox.fundro.data.local.dto.toDomain
import com.basebox.fundro.data.local.dto.toEntity
import com.basebox.fundro.data.remote.api.UserApi
import com.basebox.fundro.data.remote.dto.request.UpdateProfileRequest
import com.basebox.fundro.data.remote.dto.response.getOrThrow
import com.basebox.fundro.domain.model.SearchUser
import com.basebox.fundro.domain.model.User
import com.basebox.fundro.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userDao: UserDao,
    @ApplicationContext private val context: Context
) : UserRepository {

    private val secureStorage: SecureStorage
        get() = SecureStorage(@ApplicationContext context)
    override suspend fun searchUsers(query: String, page: Int, size: Int): Flow<ApiResult<List<SearchUser>>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = userApi.searchUsers(query, page, size)

            if (response.isSuccessful && response.body() != null) {
                val searchResponse = response.body()!!
                val users = searchResponse.content.map { userResponse ->
                    SearchUser(
                        id = userResponse.id,
                        username = userResponse.username,
                        fullName = userResponse.fullName
                    )
                }

                emit(ApiResult.Success(users))
                Timber.d("Found ${users.size} users for query: $query")
            } else {
                emit(ApiResult.Error("Failed to search users"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
            Timber.e(e, "Search users error")
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getCurrentUser(): Flow<ApiResult<User>> = flow {
        emit(ApiResult.Loading)

        try {
            val cachedUser = userDao.getUserFlow(secureStorage.getUserId()!!)
                .map { it?.toDomain() }
                .first()

            if (cachedUser != null) {
                Timber.d("📦 Emitting cached user: ${cachedUser.fullName}")
                emit(ApiResult.Success(cachedUser))
            }
            val response = userApi.getCurrentUser()

            if (response.isSuccessful && response.body() != null) {
                val userResponse = response.body()!!.getOrThrow()
                val user = User(
                    id = userResponse.id,
                    username = userResponse.username,
                    fullName = userResponse.fullName,
                    email = userResponse.email,
                    phoneNumber = userResponse.phoneNumber,
                    role = userResponse.role,
                    kycStatus = userResponse.kycStatus,
                    isActive = userResponse.isActive,
                    bankName = userResponse.bankName,
                    accountHolderName = userResponse.accountHolderName,
                    bankCode = userResponse.bankCode,
                    bankAccountNumber = userResponse.bankAccountNumber,
                    kycVerifiedAt = userResponse.kycVerifiedAt,
                    createdAt = userResponse.createdAt
                )

                Timber.d("💾 Saving user to cache: ${user.fullName}")
                userDao.insertUser(user.toEntity())

                // 4. Emit fresh data
                emit(ApiResult.Success(user))
                Timber.d("✅ Fetched and cached user")
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Session expired. Please login again."
                    else -> "Failed to get user info"
                }
                emit(ApiResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            val cachedUser = userDao.getUserFlow("current")
                .map { it?.toDomain() }
                .first()

            if (cachedUser != null) {
                emit(ApiResult.Success(cachedUser))
            } else {
                emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
                Timber.e(e, "Get current user error : ${e.localizedMessage}")
            }

        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateProfile(
        fullName: String?,
        phoneNumber: String?,
        username: String?
    ): Flow<ApiResult<User>> = flow {
        emit(ApiResult.Loading)

        try {
            val request = UpdateProfileRequest(
                fullName = fullName,
                phoneNumber = phoneNumber,
                username = username
            )

            val response = userApi.updateProfile(request)

            if (response.isSuccessful && response.body() != null) {
                val userResponse = response.body()!!.getOrThrow()
                val user = User(
                    id = userResponse.id,
                    username = userResponse.username,
                    fullName = userResponse.fullName,
                    email = userResponse.email,
                    phoneNumber = userResponse.phoneNumber,
                    role = userResponse.role,
                    kycStatus = userResponse.kycStatus,
                    isActive = userResponse.isActive,
                    bankName = userResponse.bankName,
                    accountHolderName = userResponse.accountHolderName,
                    bankCode = userResponse.bankCode,
                    bankAccountNumber = userResponse.bankAccountNumber,
                    kycVerifiedAt = userResponse.kycVerifiedAt,
                    createdAt = userResponse.createdAt
                )
                emit(ApiResult.Success(user))
                Timber.d("Profile updated: ${user.fullName}")
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Invalid profile data"
                    409 -> "Username already taken"
                    else -> "Failed to update profile: ${response.message()}"
                }
                emit(ApiResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
            Timber.e(e, "Update profile error")
        }
    }.flowOn(Dispatchers.IO)
}