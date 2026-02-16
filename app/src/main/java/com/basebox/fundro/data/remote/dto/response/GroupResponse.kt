package com.basebox.fundro.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupResponse(
    @Json(name = "id")
    val id: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "description")
    val description: String?,

    @Json(name = "targetAmount")
    val targetAmount: Double,

    @Json(name = "status")
    val status: String,

    @Json(name = "visibility")
    val visibility: String? = "PRIVATE",

    @Json(name = "category")
    val category: String? = null,

    @Json(name = "deadline")
    val deadline: String?,

    @Json(name = "createdAt")
    val createdAt: String,

    @Json(name = "owner")
    val owner: OwnerResponse,

    @Json(name = "totalCollected")
    val totalCollected: Double,

    @Json(name = "participantCount")
    val participantCount: Int,

    @Json(name = "progressPercentage")
    val progressPercentage: Double,

    @Json(name = "hasCurrentUserContributed")
    val hasCurrentUserContributed: Boolean
)