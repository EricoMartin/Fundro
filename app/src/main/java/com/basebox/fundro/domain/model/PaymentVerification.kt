package com.basebox.fundro.domain.model

data class PaymentVerification(
    val contributionId: String,
    val status: String,
    val amount: Double,
    val paidAt: String?,
    val gatewayReference: String?,
    val message: String?
)