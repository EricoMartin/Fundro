package com.basebox.fundro.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.usecase.GetCurrentUserUseCase
import com.basebox.fundro.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _logoutSuccess = MutableStateFlow(false)
    val logoutSuccess: StateFlow<Boolean> = _logoutSuccess.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }

                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                user = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                        Timber.d("Profile loaded: ${result.data.fullName}")
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        Timber.e("Failed to load profile: ${result.message}")
                    }
                }
            }
        }
    }

    fun showLogoutDialog() {
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    fun hideLogoutDialog() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingOut = true, showLogoutDialog = false) }

            logoutUseCase().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _uiState.update { it.copy(isLoggingOut = false) }
                        _logoutSuccess.value = true
                        Timber.d("Logout successful")
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoggingOut = false,
                                error = result.message
                            )
                        }
                        Timber.e("Logout failed: ${result.message}")
                    }

                    is ApiResult.Loading -> {
                        _uiState.update { it.copy(isLoggingOut = true) }
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}