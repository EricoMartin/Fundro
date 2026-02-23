package com.basebox.fundro.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.core.security.SecureStorage
import com.basebox.fundro.di.NavigationEvent
import com.basebox.fundro.di.NavigationManager
import com.basebox.fundro.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    fun onEmailOrUsernameChanged(value: String) {
        _uiState.update { it.copy(email = value, error = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, error = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun login() {
        val currentState = _uiState.value

        // Validation
        if (currentState.email.isBlank()) {
            _uiState.update { it.copy(error = "Please enter email or username") }
            return
        }

        if (currentState.password.isBlank()) {
            _uiState.update { it.copy(error = "Please enter password") }
            return
        }

        viewModelScope.launch {
            loginUseCase(
                email = currentState.email.trim(),
                password = currentState.password
            ).collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }

                    is ApiResult.Success -> {

                        _uiState.update { it.copy(isLoading = false, loginSuccess = true, error = null, showSuccessDialog = true) }
                        _loginSuccess.value = true
                        Timber.d("Login successful: ${result.data.email}")
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loginSuccess = false,
                                error = result.message
                            )
                        }
                        Timber.e("Login failed: ${result.message}")
                    }
                }
            }
        }
    }


    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}