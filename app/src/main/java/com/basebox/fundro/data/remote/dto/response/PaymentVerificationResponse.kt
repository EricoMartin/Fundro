package com.basebox.fundro.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentVerificationResponse(
    @Json(name = "contributionId")
    val contributionId: String,

    @Json(name = "status")
    val status: String,  // PENDING, COMPLETED, FAILED, REFUNDED

    @Json(name = "amount")
    val amount: Double,

    @Json(name = "paidAt")
    val paidAt: String?,

    @Json(name = "gatewayReference")
    val gatewayReference: String?,

    @Json(name = "message")
    val message: String?
)