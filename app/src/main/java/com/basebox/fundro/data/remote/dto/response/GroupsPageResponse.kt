package com.basebox.fundro.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class GroupsPageResponse(
    @Json(name = "groups")
    val groups: List<GroupResponse>,

    @Json(name = "currentPage")
    val currentPage: Int,

    @Json(name = "totalPages")
    val totalPages: Int,

    @Json(name = "totalElements")
    val totalElements: Int,

    @Json(name = "pageSize")
    val pageSize: Int,

    @Json(name = "hasNext")
    val hasNext: Boolean,

    @Json(name = "hasPrevious")
    val hasPrevious: Boolean
)