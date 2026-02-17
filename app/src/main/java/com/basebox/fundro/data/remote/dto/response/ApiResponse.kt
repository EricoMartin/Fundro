package com.basebox.fundro.data.remote.dto.response

import com.squareup.moshi.Json

data class ApiResponse<T>(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String,
    @Json(name = "data") var data: T?
)

fun <T> ApiResponse<T>.getOrThrow(): T {
    if (success && data != null) {
        // If the call was successful and data is not null, return the data.
        return data!!
    } else {
        // If the call failed or data is null, throw an exception with the server's message.
        throw Exception(message)
    }
}