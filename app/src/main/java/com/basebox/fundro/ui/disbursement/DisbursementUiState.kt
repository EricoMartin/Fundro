package com.basebox.fundro.presentation.disbursement

data class DisbursementUiState(
    val isProcessing: Boolean = true,
    val isSuccess: Boolean = false,
    val isFailed: Boolean = false,
    val amount: Double? = null,
    val recipientName: String? = null,
    val recipientAccount: String? = null,
    val disbursedAt: String? = null,
    val error: String? = null
)