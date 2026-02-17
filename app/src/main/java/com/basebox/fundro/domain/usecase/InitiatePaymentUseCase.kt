package com.basebox.fundro.domain.usecase

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.PaymentInitiation
import com.basebox.fundro.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InitiatePaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(
        groupId: String,
        amount: Double
    ): Flow<ApiResult<PaymentInitiation>> {
        return paymentRepository.initiatePayment(groupId, amount)
    }
}