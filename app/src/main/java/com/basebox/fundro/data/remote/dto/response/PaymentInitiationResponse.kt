package com.basebox.fundro.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentInitiationResponse(
    @Json(name = "contributionId")
    val contributionId: String,

    @Json(name = "authorizationUrl")
    val authorizationUrl: String,

    @Json(name = "accessCode")
    val accessCode: String,

    @Json(name = "reference")
    val reference: String,

    @Json(name = "amount")
    val amount: Double,

    @Json(name = "groupId")
    val groupId: String,

    @Json(name = "groupName")
    val groupName: String
)