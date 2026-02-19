package com.basebox.fundro.data.remote.api

import com.basebox.fundro.data.remote.dto.request.InitiatePaymentRequest
import com.basebox.fundro.data.remote.dto.request.KycRequest
import com.basebox.fundro.data.remote.dto.response.ApiResponse
import com.basebox.fundro.data.remote.dto.response.KycResponse
import com.basebox.fundro.data.remote.dto.response.PaymentInitiationResponse
import com.basebox.fundro.data.remote.dto.response.PaymentVerificationResponse
import retrofit2.Response
import retrofit2.http.*

interface PaymentApi {

    @POST("contributions/initiate")
    suspend fun initiatePayment(
        @Body request: InitiatePaymentRequest
    ): Response<ApiResponse<PaymentInitiationResponse>>

    @GET("contributions/{contributionId}/verify")
    suspend fun verifyPayment(
        @Path("contributionId") contributionId: String
    ): Response<ApiResponse<PaymentVerificationResponse>>

    @POST("kyc/submit")
    suspend fun submitKyc(
        @Body request: KycRequest
    ): Response<ApiResponse<KycResponse>>
}