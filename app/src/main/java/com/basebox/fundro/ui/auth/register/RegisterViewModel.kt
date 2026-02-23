package com.basebox.fundro.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.core.util.isValidEmail
import com.basebox.fundro.core.util.isValidPassword
import com.basebox.fundro.core.util.isValidPhoneNumber
import com.basebox.fundro.core.util.isValidUsername
import com.basebox.fundro.di.NavigationEvent
import com.basebox.fundro.di.NavigationManager
import com.basebox.fundro.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess.asStateFlow()

    fun onUsernameChanged(value: String) {
        _uiState.update { it.copy(username = value, usernameError = null, error = null) }
    }

    fun onFullNameChanged(value: String) {
        _uiState.update { it.copy(fullName = value, error = null) }
    }

    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value, emailError = null, error = null) }
    }

    fun onPhoneNumberChanged(value: String) {
        _uiState.update { it.copy(phoneNumber = value, phoneError = null, error = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, error = null) }
    }

    fun onConfirmPasswordChanged(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null, error = null) }
    }

    fun onBvnChanged(value: String) {
        _uiState.update { it.copy(bvn = value, bvnError = null, error = null) }
    }

    fun onBankNameChanged(value: String) {
        _uiState.update { it.copy(bankName = value, bankNameError = null, error = null) }
    }

    fun onAccountNumberChanged(value: String) {
        _uiState.update { it.copy(bankAccountNumber = value, bankAccountNumberError = null, error = null) }
    }

    fun onAccountHolderNameChanged(value: String) {
        _uiState.update { it.copy(accountHolderName = value, accountHolderNameError = null, error = null) }
    }

    fun onBankCodeChanged(value: String) {
        _uiState.update { it.copy(bankCode = value, bankCodeError = null, error = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun register() {
        val currentState = _uiState.value

        // Validate all fields
        var hasError = false

        // Basic info validation
        if (currentState.username.isBlank()) {
            _uiState.update { it.copy(usernameError = "Username is required") }
            hasError = true
        } else if (!currentState.username.isValidUsername()) {
            _uiState.update { it.copy(usernameError = "Username must be 3-50 characters (letters, numbers, underscore only)") }
            hasError = true
        }

        if (currentState.fullName.isBlank()) {
            _uiState.update { it.copy(error = "Full name is required") }
            hasError = true
        }

        if (currentState.email.isBlank()) {
            _uiState.update { it.copy(emailError = "Email is required") }
            hasError = true
        } else if (!currentState.email.isValidEmail()) {
            _uiState.update { it.copy(emailError = "Invalid email format") }
            hasError = true
        }

        if (currentState.phoneNumber.isBlank()) {
            _uiState.update { it.copy(phoneError = "Phone number is required") }
            hasError = true
        } else if (!currentState.phoneNumber.isValidPhoneNumber()) {
            _uiState.update { it.copy(phoneError = "Invalid phone number (use +234...)") }
            hasError = true
        }

        if (currentState.password.isBlank()) {
            _uiState.update { it.copy(passwordError = "Password is required") }
            hasError = true
        } else if (!currentState.password.isValidPassword()) {
            _uiState.update { it.copy(passwordError = "Password must be 8+ characters with uppercase, lowercase, number, and special character") }
            hasError = true
        }

        if (currentState.confirmPassword.isBlank()) {
            _uiState.update { it.copy(confirmPasswordError = "Please confirm your password") }
            hasError = true
        } else if (currentState.password != currentState.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Passwords do not match") }
            hasError = true
        }

        // Bank info validation
        if (currentState.bvn.isBlank()) {
            _uiState.update { it.copy(bvnError = "BVN is required") }
            hasError = true
        } else if (currentState.bvn.length != 11) {
            _uiState.update { it.copy(bvnError = "BVN must be 11 digits") }
            hasError = true
        }

        if (currentState.bankName.isBlank()) {
            _uiState.update { it.copy(bankNameError = "Bank name is required") }
            hasError = true
        }

        if (currentState.bankAccountNumber.isBlank()) {
            _uiState.update { it.copy(bankAccountNumberError = "Account number is required") }
            hasError = true
        } else if (currentState.bankAccountNumber.length != 10) {
            _uiState.update { it.copy(bankAccountNumberError = "Account number must be 10 digits") }
            hasError = true
        }

        if (currentState.accountHolderName.isBlank()) {
            _uiState.update { it.copy(accountHolderNameError = "Account holder name is required") }
            hasError = true
        }

        if (currentState.bankCode.isBlank()) {
            _uiState.update { it.copy(bankCodeError = "Bank code is required") }
            hasError = true
        }

        if (hasError) return

        // Proceed with registration
        viewModelScope.launch {
            registerUseCase(
                username = currentState.username.trim(),
                fullName = currentState.fullName.trim(),
                email = currentState.email.trim(),
                password = currentState.password,
                phoneNumber = currentState.phoneNumber.trim(),
                bvn = currentState.bvn.trim(),
                bankName = currentState.bankName.trim(),
                bankAccountNumber = currentState.bankAccountNumber.trim(),
                accountHolderName = currentState.accountHolderName.trim(),
                role = "user",
                bankCode = currentState.bankCode.trim()
            ).collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }

                    is ApiResult.Success -> {
                        _uiState.update { it.copy(isLoading = false, registerSuccess = true, error = null, showSuccessDialog = true) }
                        _registerSuccess.value = true
                        Timber.d("Registration successful: ${result.data.email}")
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        Timber.e("Registration failed: ${result.message}")
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}