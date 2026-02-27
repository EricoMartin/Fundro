package com.basebox.fundro.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DisbursementResponse(
    @Json(name = "disbursementId")
    val disbursementId: String,

    @Json(name = "groupId")
    val groupId: String,

    @Json(name = "groupName")
    val groupName: String,

    @Json(name = "amount")
    val amount: Double,

    @Json(name = "recipientName")
    val recipientName: String,

    @Json(name = "recipientAccount")
    val recipientAccount: String?,

    @Json(name = "disbursedAt")
    val disbursedAt: String,

    @Json(name = "status")
    val status: String,

    @Json(name = "message")
    val message: String
)