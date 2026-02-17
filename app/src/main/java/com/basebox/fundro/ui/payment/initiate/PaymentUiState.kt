package com.basebox.fundro.ui.payment.initiate

import com.basebox.fundro.domain.model.Group

data class PaymentUiState(
    val group: Group? = null,
    val amount: String = "",
    val amountError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val paymentInitiated: Boolean = false
)