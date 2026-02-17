package com.basebox.fundro.domain.usecase

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.User
import com.basebox.fundro.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        fullName: String? = null,
        phoneNumber: String? = null,
        username: String? = null
    ): Flow<ApiResult<User>> {
        return userRepository.updateProfile(
            fullName = fullName,
            phoneNumber = phoneNumber,
            username = username
        )
    }
}