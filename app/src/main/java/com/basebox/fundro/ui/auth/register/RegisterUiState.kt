package com.basebox.fundro.ui.auth.register

data class RegisterUiState(
    val username: String = "",
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val bvn: String = "",
    val bankName: String = "",
    val bankAccountNumber: String = "",
    val accountHolderName: String = "",
    val bankCode: String = "",

    // Validation errors
    val usernameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val bvnError: String? = null,
    val bankNameError: String? = null,
    val bankAccountNumberError: String? = null,
    val accountHolderNameError: String? = null,
    val bankCodeError: String? = null,
    val showSuccessDialog: Boolean = false,
)