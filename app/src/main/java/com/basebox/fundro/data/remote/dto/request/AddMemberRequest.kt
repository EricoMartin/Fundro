package com.basebox.fundro.data.remote.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddMemberRequest(
    @Json(name = "userId")
    val userId: String,

    @Json(name = "expectedAmount")
    val expectedAmount: Double?
)

@JsonClass(generateAdapter = true)
data class AddMembersRequest(
    @Json(name = "userIds")
    val userIds: List<String>,

    @Json(name = "expectedAmount")
    val expectedAmount: Double?
)