package com.basebox.fundro.domain.model

data class Contribution(
    val id: String,
    val groupId: String,
    val groupName: String,
    val amount: Double,
    val paymentStatus: String,
    val gatewayReference: String?,
    val paidAt: String?,
    val createdAt: String
)