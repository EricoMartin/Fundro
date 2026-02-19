package com.basebox.fundro.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KycResponse(
    @Json(name = "status")
    val status: String,
    @Json(name = "message")
    val message: String
)

//{
//    "success": true,
//    "message": "KYC submitted and verified",
//    "data": {
//        "status": "VERIFIED",
//        "message": "KYC verified successfully"
//    }
//}