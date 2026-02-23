package com.basebox.fundro.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentVerificationResponse(
    @Json(name = "id")
    val contributionId: String,

    @Json(name = "groupId")
    val groupId: String,

    @Json(name = "paymentStatus")
    val status: String,  // PENDING, COMPLETED, FAILED, REFUNDED

    @Json(name = "amount")
    val amount: Double,

    @Json(name = "groupName")
    val groupName: String,

    @Json(name = "paidAt")
    val paidAt: String?,

    @Json(name = "gatewayReference")
    val gatewayReference: String?,

    @Json(name = "message")
    val message: String?,

    @Json(name = "createdAt")
    val createdAt: String
)