package com.basebox.fundro.domain.model
data class Disbursement(
    val disbursementId: String,
    val groupId: String,
    val groupName: String,
    val amount: Double,
    val recipientName: String,
    val recipientAccount: String?,
    val disbursedAt: String,
    val status: String,
    val message: String
)