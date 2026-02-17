package com.basebox.fundro.domain.usecase

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.PaymentVerification
import com.basebox.fundro.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(contributionId: String): Flow<ApiResult<PaymentVerification>> {
        return paymentRepository.verifyPayment(contributionId)
    }
}