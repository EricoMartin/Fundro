package com.basebox.fundro.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserSearchResponse(
    @Json(name = "id")
    val id: String,

    @Json(name = "username")
    val username: String,

    @Json(name = "fullName")
    val fullName: String
)