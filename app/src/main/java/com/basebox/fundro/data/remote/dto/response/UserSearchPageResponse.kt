package com.basebox.fundro.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserSearchPageResponse(
    @Json(name = "content")
    val content: List<UserSearchResponse>,

    @Json(name = "totalElements")
    val totalElements: Int,

    @Json(name = "totalPages")
    val totalPages: Int,

    @Json(name = "number")
    val number: Int,

    @Json(name = "size")
    val size: Int
)