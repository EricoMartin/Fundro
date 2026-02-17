package com.basebox.fundro.data.remote.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InitiatePaymentRequest(
    @Json(name = "groupId")
    val groupId: String,

    @Json(name = "amount")
    val amount: Double
)