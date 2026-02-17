package com.basebox.fundro.data.remote.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateProfileRequest(
    @Json(name = "fullName")
    val fullName: String? = null,

    @Json(name = "phoneNumber")
    val phoneNumber: String? = null,

    @Json(name = "username")
    val username: String? = null
)