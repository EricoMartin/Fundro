package com.basebox.fundro.ui.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.core.util.isValidPhoneNumber
import com.basebox.fundro.core.util.isValidUsername
import com.basebox.fundro.domain.usecase.GetCurrentUserUseCase
import com.basebox.fundro.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { result ->
                if (result is ApiResult.Success) {
                    _uiState.update {
                        it.copy(
                            fullName = result.data.fullName,
                            username = result.data.username,
                            phoneNumber = result.data.phoneNumber
                        )
                    }
                }
            }
        }
    }

    fun onFullNameChanged(value: String) {
        _uiState.update { it.copy(fullName = value, fullNameError = null) }
    }

    fun onUsernameChanged(value: String) {
        _uiState.update { it.copy(username = value, usernameError = null) }
    }

    fun onPhoneNumberChanged(value: String) {
        _uiState.update { it.copy(phoneNumber = value, phoneError = null) }
    }

    fun saveProfile() {
        val currentState = _uiState.value
        var hasError = false

        // Validate full name
        if (currentState.fullName.isBlank()) {
            _uiState.update { it.copy(fullNameError = "Full name is required") }
            hasError = true
        } else if (currentState.fullName.length < 2) {
            _uiState.update { it.copy(fullNameError = "Full name is too short") }
            hasError = true
        }

        // Validate username
        if (currentState.username.isBlank()) {
            _uiState.update { it.copy(usernameError = "Username is required") }
            hasError = true
        } else if (!currentState.username.isValidUsername()) {
            _uiState.update {
                it.copy(usernameError = "Username must be 3-50 characters (letters, numbers, underscore)")
            }
            hasError = true
        }

        // Validate phone number
        if (currentState.phoneNumber.isBlank()) {
            _uiState.update { it.copy(phoneError = "Phone number is required") }
            hasError = true
        } else if (!currentState.phoneNumber.isValidPhoneNumber()) {
            _uiState.update { it.copy(phoneError = "Invalid phone number (use +234...)") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            updateProfileUseCase(
                fullName = currentState.fullName.trim(),
                username = currentState.username.trim(),
                phoneNumber = currentState.phoneNumber.trim()
            ).collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }

                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = null,
                                isSaved = true
                            )
                        }
                        Timber.d("Profile updated: ${result.data.fullName}")
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        Timber.e("Update failed: ${result.message}")
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}