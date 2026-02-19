package com.basebox.fundro.ui.profile.kyc

data class KycUiState(
    val bvn: String = "",
    val accountNumber: String = "",
    val bankCode: String = "",
    val accountHolderName: String = "",
    val isBvnVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
    )