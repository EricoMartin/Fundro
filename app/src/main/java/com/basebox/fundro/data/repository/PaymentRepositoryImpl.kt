package com.basebox.fundro.data.repository

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.data.remote.api.PaymentApi
import com.basebox.fundro.data.remote.dto.request.InitiatePaymentRequest
import com.basebox.fundro.data.remote.dto.response.getOrThrow
import com.basebox.fundro.domain.model.PaymentInitiation
import com.basebox.fundro.domain.model.PaymentVerification
import com.basebox.fundro.domain.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val paymentApi: PaymentApi
) : PaymentRepository {

    override suspend fun initiatePayment(
        groupId: String,
        amount: Double
    ): Flow<ApiResult<PaymentInitiation>> = flow {
        emit(ApiResult.Loading)

        try {
            val request = InitiatePaymentRequest(
                groupId = groupId,
                amount = amount
            )

            val response = paymentApi.initiatePayment(request)

            if (response.isSuccessful && response.body() != null) {
                val paymentResponse = response.body()!!.getOrThrow()
                val paymentInitiation = PaymentInitiation(
                    contributionId = paymentResponse.contributionId,
                    authorizationUrl = paymentResponse.authorizationUrl,
                    accessCode = paymentResponse.accessCode,
                    reference = paymentResponse.reference,
                    amount = paymentResponse.amount,
                    groupId = paymentResponse.groupId,
                    groupName = paymentResponse.groupName
                )

                emit(ApiResult.Success(paymentInitiation))
                Timber.d("Payment initiated: ${paymentInitiation.reference}")
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Invalid payment details"
                    401 -> "Session expired. Please login again."
                    403 -> "You cannot contribute to this group"
                    409 -> "You have already contributed to this group"
                    else -> "Failed to initiate payment: ${response.message()}"
                }
                emit(ApiResult.Error(errorMessage, response.code()))
                Timber.e("Payment initiation failed: $errorMessage")
            }
        } catch (e: Exception) {
            val errorMessage = "Network error: ${e.localizedMessage}"
            emit(ApiResult.Error(errorMessage))
            Timber.e(e, "Payment initiation error")
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun verifyPayment(contributionId: String): Flow<ApiResult<PaymentVerification>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = paymentApi.verifyPayment(contributionId)

            if (response.isSuccessful && response.body() != null) {
                val verificationResponse = response.body()!!.getOrThrow()
                val verification = PaymentVerification(
                    contributionId = verificationResponse.contributionId,
                    status = verificationResponse.status,
                    amount = verificationResponse.amount,
                    paidAt = verificationResponse.paidAt,
                    gatewayReference = verificationResponse.gatewayReference,
                    message = verificationResponse.message
                )

                emit(ApiResult.Success(verification))
                Timber.d("Payment verification: ${verification.status}")
            } else {
                emit(ApiResult.Error("Failed to verify payment"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
            Timber.e(e, "Payment verification error")
        }
    }.flowOn(Dispatchers.IO)
}