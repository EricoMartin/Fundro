package com.basebox.fundro.data.remote.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FcmTokenRequest(
    @Json(name = "token")
    val token: String,

    @Json(name = "platform")
    val platform: String = "ANDROID"
)