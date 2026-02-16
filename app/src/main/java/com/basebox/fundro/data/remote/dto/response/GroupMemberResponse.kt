package com.basebox.fundro.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupMemberResponse(
    @Json(name = "id")
    val id: String,

    @Json(name = "userId")
    val userId: String,

    @Json(name = "username")
    val username: String,

    @Json(name = "fullName")
    val fullName: String,

    @Json(name = "status")
    val status: String,  // INVITED, JOINED, PAID, REMOVED

    @Json(name = "expectedAmount")
    val expectedAmount: Double?,

    @Json(name = "paidAmount")
    val paidAmount: Double?,

    @Json(name = "invitedAt")
    val invitedAt: String,

    @Json(name = "joinedAt")
    val joinedAt: String?,

    @Json(name = "paidAt")
    val paidAt: String?
)