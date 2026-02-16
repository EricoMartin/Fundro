package com.basebox.fundro.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    @param:Json(name = "id")
    val id: String,

    @param:Json(name = "username")
    val username: String,

    @param:Json(name = "fullName")
    val fullName: String,

    @param:Json(name = "email")
    val email: String,

    @param:Json(name = "phoneNumber")
    val phoneNumber: String,

    @param:Json(name = "role")
    val role: String,

    @param:Json(name = "kycStatus")
    val kycStatus: String,

    @param:Json(name = "bvn")
    val bvn: String = "",

    @param:Json(name = "kycVerifiedAt")
    val kycVerifiedAt: String? = null,

    @param:Json(name = "isActive")
    val isActive: Boolean,

    @param:Json(name = "createdAt")
    val createdAt: String,

    @param:Json(name = "bankAccountNumber")
    val bankAccountNumber: String? = null,

    @param:Json(name = "bankCode")
    val bankCode: String? = null,

    @param:Json(name = "bankName")
    val bankName: String? = null,

    @param:Json(name = "accountHolderName")
    val accountHolderName: String? = null
)