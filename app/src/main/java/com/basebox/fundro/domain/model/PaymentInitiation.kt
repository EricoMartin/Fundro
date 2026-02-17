package com.basebox.fundro.domain.model

data class PaymentInitiation(
    val contributionId: String,
    val authorizationUrl: String,
    val accessCode: String,
    val reference: String,
    val amount: Double,
    val groupId: String,
    val groupName: String
)