package com.basebox.fundro.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContributionResponse(
    @Json(name = "id")
    val id: String,

    @Json(name = "groupId")
    val groupId: String,

    @Json(name = "groupName")
    val groupName: String,

    @Json(name = "amount")
    val amount: Double,

    @Json(name = "paymentStatus")
    val paymentStatus: String,  // PENDING, COMPLETED, FAILED, REFUNDED

    @Json(name = "gatewayReference")
    val gatewayReference: String?,

    @Json(name = "paidAt")
    val paidAt: String?,

    @Json(name = "createdAt")
    val createdAt: String
)