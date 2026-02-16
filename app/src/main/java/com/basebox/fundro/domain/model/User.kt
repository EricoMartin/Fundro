package com.basebox.fundro.domain.model

data class User(
    val id: String,
    val username: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val role: String,
    val kycStatus: String,
    val isActive: Boolean,
    val bvn: String? = "",
    val bankAccountNumber: String?,
    val bankCode: String?,
    val bankName: String?,
    val accountHolderName: String?,
    val paystackRecipientCode: String = "",
    val kycRejectionReason: String = ""
)



