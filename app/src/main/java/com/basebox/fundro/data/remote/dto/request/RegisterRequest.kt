package com.basebox.fundro.data.remote.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    @param:Json(name = "username")
    val username: String,

    @param:Json(name = "fullName")
    val fullName: String,

    @param:Json(name = "email")
    val email: String,

    @param:Json(name = "password")
    val password: String,

    @param:Json(name = "phoneNumber")
    val phoneNumber: String,

    @param:Json(name = "bvn")
    val bvn: String,

    @param:Json(name = "bankAccountNumber")
    val bankAccountNumber: String,

    @param:Json(name = "bankName")
    val bankName: String,

    @param:Json(name = "bankCode")
    val bankCode: String,

    @param:Json(name = "role")
    val role: String = "USER",

    @param:Json(name = "accountHolderName")
    val accountHolderName: String
)

