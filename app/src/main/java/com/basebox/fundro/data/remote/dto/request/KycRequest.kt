package com.basebox.fundro.data.remote.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KycRequest(
    @Json(name = "bvn")
    val bvn: String,
    @Json(name = "accountNumber")
    val accountNumber: String,
    @Json(name = "bankCode")
    val bankCode: String,
    @Json(name = "accountHolderName")
    val accountHolderName: String
)