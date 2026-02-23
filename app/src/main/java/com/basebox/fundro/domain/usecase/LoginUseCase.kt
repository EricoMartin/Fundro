package com.basebox.fundro.domain.usecase

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.data.remote.dto.response.UserResponse
import com.basebox.fundro.domain.model.User
import com.basebox.fundro.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Flow<ApiResult<User>> {
        return authRepository.login(email, password)
    }
}