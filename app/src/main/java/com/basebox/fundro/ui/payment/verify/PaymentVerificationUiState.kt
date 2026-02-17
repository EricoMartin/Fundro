package com.basebox.fundro.ui.payment.verify

data class PaymentVerificationUiState(
    val isVerifying: Boolean = true,
    val verificationAttempt: Int = 0,
    val maxAttempts: Int = 10,
    val status: String? = null,
    val amount: Double? = null,
    val paidAt: String? = null,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val isFailed: Boolean = false
)