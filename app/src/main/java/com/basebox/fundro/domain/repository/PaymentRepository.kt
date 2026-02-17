package com.basebox.fundro.domain.repository

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.PaymentInitiation
import com.basebox.fundro.domain.model.PaymentVerification
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    suspend fun initiatePayment(
        groupId: String,
        amount: Double
    ): Flow<ApiResult<PaymentInitiation>>

    suspend fun verifyPayment(
        contributionId: String
    ): Flow<ApiResult<PaymentVerification>>
}