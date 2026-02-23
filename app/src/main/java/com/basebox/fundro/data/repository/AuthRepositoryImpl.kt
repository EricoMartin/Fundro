package com.basebox.fundro.data.repository

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.core.security.SecureStorage
import com.basebox.fundro.data.local.dao.UserDao
import com.basebox.fundro.data.local.dto.toEntity
import com.basebox.fundro.data.remote.api.AuthApi
import com.basebox.fundro.data.remote.dto.request.LoginRequest
import com.basebox.fundro.data.remote.dto.request.RegisterRequest
import com.basebox.fundro.data.remote.dto.response.getOrThrow
import com.basebox.fundro.domain.model.User
import com.basebox.fundro.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val secureStorage: SecureStorage,
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun register(
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
        accountHolderName: String
    ): Flow<ApiResult<User>> = flow {
        emit(ApiResult.Loading)

        try {
            val request = RegisterRequest(
                username = username,
                fullName = fullName,
                email = email,
                password = password,
                phoneNumber = phoneNumber,
                bvn = bvn,
                role = role,
                bankAccountNumber = bankAccountNumber,
                bankName = bankName,
                bankCode = bankCode,
                accountHolderName = accountHolderName
            )

            val response = authApi.register(request)

            if (response.isSuccessful && response.body() != null) {
                val registerResponse = response.body() ?: throw Exception("Empty response body")

                val userResponse = registerResponse.user

                // Save user info
                secureStorage.saveUserId(userResponse.id)
                secureStorage.saveUserEmail(userResponse.email)

                val user = User(
                    id = userResponse.id,
                    username = userResponse.username,
                    fullName = userResponse.fullName,
                    email = userResponse.email,
                    phoneNumber = userResponse.phoneNumber,
                    role = userResponse.role,
                    kycStatus = userResponse.kycStatus,
                    isActive = userResponse.isActive,
                    bvn = userResponse.bvn,
                    bankAccountNumber = userResponse.bankAccountNumber,
                    bankCode = userResponse.bankCode,
                    bankName = userResponse.bankName,
                    accountHolderName = userResponse.accountHolderName,
                    createdAt = userResponse.createdAt,
                )

                emit(ApiResult.Success(user))
                Timber.d("Registration successful: ${user.email}")
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Invalid registration details"
                    409 -> "Email or username already exists"
                    else -> "Registration failed: ${response.message()}"
                }
                emit(ApiResult.Error(errorMessage, response.code()))
                Timber.e("Registration failed: $errorMessage")
            }
        } catch (e: Exception) {
            val errorMessage = "Network error: ${e.localizedMessage}"
            Timber.e(e, "Registration error - response body: ${errorMessage}")
            emit(ApiResult.Error(errorMessage))
            Timber.e(e, "Registration error")
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun login(
        email: String,
        password: String
    ): Flow<ApiResult<User>> = flow {
        emit(ApiResult.Loading)

        try {
            val request = LoginRequest(
                email = email,
                password = password
            )

            val response = authApi.login(request)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!

                // Save JWT token
                secureStorage.saveAccessToken(authResponse.accessToken)

                // Save user info
                val userResponse = authResponse.user
                secureStorage.saveUserId(userResponse.id)
                secureStorage.saveUserEmail(userResponse.email)

                val user = User(
                    id = userResponse.id,
                    username = userResponse.username,
                    fullName = userResponse.fullName,
                    email = userResponse.email,
                    phoneNumber = userResponse.phoneNumber,
                    role = userResponse.role,
                    kycStatus = userResponse.kycStatus,
                    isActive = userResponse.isActive,
                    bvn = userResponse.bvn,
                    bankAccountNumber = userResponse.bankAccountNumber,
                    bankCode = userResponse.bankCode,
                    bankName = userResponse.bankName,
                    accountHolderName = userResponse.accountHolderName,
                    createdAt = userResponse.createdAt,
                )

                emit(ApiResult.Success(user))
                Timber.d("Login successful: ${user.email}")
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Invalid email/username or password"
                    403 -> "Account is inactive"
                    else -> "Login failed: ${response.message()}"
                }
                emit(ApiResult.Error(errorMessage, response.code()))
                Timber.e("Login failed: $errorMessage")
            }
        } catch (e: Exception) {
            val errorMessage = "Network error: ${e.localizedMessage}"
            emit(ApiResult.Error(errorMessage))
            Timber.e(e, "Login error")
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getCurrentUser(): Flow<ApiResult<User>> = flow {
        emit(ApiResult.Loading)

        try {
            val userId = secureStorage.getUserId() ?: throw Exception("User ID not found")

            val currentLocalUser = userDao.getUser(userId)

            if (currentLocalUser != null) {
                val user = User(
                    id = currentLocalUser.id,
                    username = currentLocalUser.username,
                    fullName = currentLocalUser.fullName,
                    email = currentLocalUser.email,
                    phoneNumber = currentLocalUser.phoneNumber,
                    role = currentLocalUser.role,
                    kycStatus = currentLocalUser.kycStatus,
                    isActive = currentLocalUser.isActive,
                    bvn = currentLocalUser.bvn,
                    bankAccountNumber = currentLocalUser.bankAccountNumber,
                    bankCode = currentLocalUser.bankCode,
                    bankName = currentLocalUser.bankName,
                    accountHolderName = currentLocalUser.accountHolderName,
                    createdAt = currentLocalUser.createdAt?: "",
                    )
                emit(ApiResult.Success(user))
                return@flow
            }

            val response = authApi.getCurrentUser()

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
                    bvn = userResponse.bvn,
                    bankAccountNumber = userResponse.bankAccountNumber,
                    bankCode = userResponse.bankCode,
                    bankName = userResponse.bankName,
                    accountHolderName = userResponse.accountHolderName,
                    createdAt = userResponse.createdAt,
                )
                userDao.deleteUser(user.id)
                userDao.insertUser(user.toEntity())

                emit(ApiResult.Success(user))
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Session expired. Please login again."
                    else -> "Failed to get user info: ${response.message()}"
                }
                emit(ApiResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            val userId = secureStorage.getUserId() ?: throw Exception("User ID not found")

            val currentLocalUser = userDao.getUser(userId)

            if (currentLocalUser != null) {
                val user = User(
                    id = currentLocalUser.id,
                    username = currentLocalUser.username,
                    fullName = currentLocalUser.fullName,
                    email = currentLocalUser.email,
                    phoneNumber = currentLocalUser.phoneNumber,
                    role = currentLocalUser.role,
                    kycStatus = currentLocalUser.kycStatus,
                    isActive = currentLocalUser.isActive,
                    bvn = currentLocalUser.bvn,
                    bankAccountNumber = currentLocalUser.bankAccountNumber,
                    bankCode = currentLocalUser.bankCode,
                    bankName = currentLocalUser.bankName,
                    accountHolderName = currentLocalUser.accountHolderName,
                    createdAt = currentLocalUser.createdAt?: "",
                )
                emit(ApiResult.Success(user))
                Timber.d("📦 Emitted $user cached user due to network error")
                return@flow
            }
            val errorMessage = "Network error: ${e.localizedMessage}"
            emit(ApiResult.Error(errorMessage))
            Timber.e(e, "Get current user error")
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun logout(): Flow<ApiResult<Unit>> = flow {
        emit(ApiResult.Loading)

        try {
            secureStorage.clearAll()
            emit(ApiResult.Success(Unit))
            Timber.d("Logout successful")
        } catch (e: Exception) {
            emit(ApiResult.Error("Logout failed: ${e.localizedMessage}"))
            Timber.e(e, "Logout error")
        }
    }.flowOn(Dispatchers.IO)

    override fun isLoggedIn(): Boolean {
        return secureStorage.isLoggedIn()
    }

    override fun isOnboardingCompleted(): Boolean {
        return secureStorage.isOnboardingCompleted()
    }
}