package com.basebox.fundro.ui.auth.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val loginSuccess: Boolean = false,
    val error: String? = null
)