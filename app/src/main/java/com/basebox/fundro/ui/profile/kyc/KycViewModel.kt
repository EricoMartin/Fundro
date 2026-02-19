package com.basebox.fundro.ui.profile.kyc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.usecase.KycVerificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KycViewModel @Inject constructor(
    private val kycVerificationUseCase: KycVerificationUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(KycUiState())
    val uiState: StateFlow<KycUiState> = _uiState.asStateFlow()

    fun onFullNameChanged(value: String) {
        _uiState.update { it.copy(accountHolderName = value, error = null) }
    }

    fun onBvnChanged(value: String) {
        _uiState.update { it.copy(bvn = value, error = null) }
    }

    fun onAccountNumberChanged(value: String) {
        _uiState.update { it.copy(accountNumber = value, error = null) }
    }

    fun onBankCodeChanged(value: String) {
        _uiState.update { it.copy(bankCode = value, error = null) }
    }

    fun toggleBvnVisibility() {
        _uiState.update { it.copy(isBvnVisible = !it.isBvnVisible) }
    }

    fun verifyKyc() {
        val bvn = _uiState.value.bvn
        val accountNumber = _uiState.value.accountNumber
        val bankCode = _uiState.value.bankCode
        val accountHolderName = _uiState.value.accountHolderName
        viewModelScope.launch {
            kycVerificationUseCase(bvn, accountNumber, bankCode, accountHolderName).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = null
                            )
                        }
                        Timber.d("Kyc verification successful")
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        Timber.e("Kyc verification failed: ${result.message}")

                    }

                    is ApiResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}