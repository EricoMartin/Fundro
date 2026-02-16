package com.basebox.fundro.data.remote.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @param:Json(name = "email")
    val email: String,

    @param:Json(name = "password")
    val password: String
)