package com.basebox.fundro.domain.usecase

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.User
import com.basebox.fundro.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
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
    ): Flow<ApiResult<User>> {
        return authRepository.register(
            username = username,
            fullName = fullName,
            email = email,
            password = password,
            phoneNumber = phoneNumber,
            role = "USER",
            bvn = bvn,
            bankAccountNumber = bankAccountNumber,
            bankCode = bankCode,
            bankName = bankName,
            accountHolderName = accountHolderName
        )
    }
}