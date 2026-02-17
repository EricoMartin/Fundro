package com.basebox.fundro.data.remote.api

import com.basebox.fundro.data.remote.dto.request.InitiatePaymentRequest
import com.basebox.fundro.data.remote.dto.response.PaymentInitiationResponse
import com.basebox.fundro.data.remote.dto.response.PaymentVerificationResponse
import retrofit2.Response
import retrofit2.http.*

interface PaymentApi {

    @POST("contributions/initiate")
    suspend fun initiatePayment(
        @Body request: InitiatePaymentRequest
    ): Response<PaymentInitiationResponse>

    @GET("contributions/{contributionId}/verify")
    suspend fun verifyPayment(
        @Path("contributionId") contributionId: String
    ): Response<PaymentVerificationResponse>
}