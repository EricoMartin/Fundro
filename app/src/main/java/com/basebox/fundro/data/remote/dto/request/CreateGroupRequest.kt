package com.basebox.fundro.data.remote.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateGroupRequest(
    @Json(name = "name")
    val name: String,

    @Json(name = "description")
    val description: String?,

    @Json(name = "targetAmount")
    val targetAmount: Double,

    @Json(name = "deadline")
    val deadline: String?,  // ISO format: "2024-12-31T23:59:59"

    @Json(name = "visibility")
    val visibility: String = "PRIVATE",  // PRIVATE, PUBLIC, UNLISTED

    @Json(name = "category")
    val category: String?,  // SUBSCRIPTION, CAMPAIGN, GIFT, EVENT, GENERAL

    @Json(name = "tags")
    val tags: String?,  // Comma-separated

    @Json(name = "maxMembers")
    val maxMembers: Int?,

    @Json(name = "generateJoinCode")
    val generateJoinCode: Boolean = false
)