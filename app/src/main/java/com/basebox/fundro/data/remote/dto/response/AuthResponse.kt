package com.basebox.fundro.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponse(
    @param:Json(name = "accessToken")
    val accessToken: String,

    @param:Json(name = "tokenType")
    val tokenType: String,

    @param:Json(name = "expiresIn")
    val expiresIn: Long,

    @param:Json(name = "user")
    val user: UserResponse
)