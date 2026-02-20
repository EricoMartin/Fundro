package com.basebox.fundro.ui.payment.initiate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.PaymentInitiation
import com.basebox.fundro.domain.usecase.GetGroupDetailsUseCase
import com.basebox.fundro.domain.usecase.InitiatePaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val getGroupDetailsUseCase: GetGroupDetailsUseCase,
    private val initiatePaymentUseCase: InitiatePaymentUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val groupId: String = checkNotNull(savedStateHandle["groupId"])

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    private val _paymentInitiation = MutableStateFlow<PaymentInitiation?>(null)
    val paymentInitiation: StateFlow<PaymentInitiation?> = _paymentInitiation.asStateFlow()

    init {
        loadGroupDetails()
    }

    private fun loadGroupDetails() {
        viewModelScope.launch {
            getGroupDetailsUseCase(groupId).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _uiState.update { it.copy(group = result.data) }
                        Timber.d("GroupEntity loaded: ${result.data.name}")
                    }

                    is ApiResult.Error -> {
                        _uiState.update { it.copy(error = result.message) }
                    }

                    is ApiResult.Loading -> {
                        // Loading handled by initial state
                    }
                }
            }
        }
    }

    fun onAmountChanged(value: String) {
        _uiState.update { it.copy(amount = value, amountError = null) }
    }

    fun initiatePayment() {
        val currentState = _uiState.value
        val group = currentState.group ?: return

        // Validation
        if (currentState.amount.isBlank()) {
            _uiState.update { it.copy(amountError = "Please enter an amount") }
            return
        }

        val amount = currentState.amount.toDoubleOrNull()
        if (amount == null) {
            _uiState.update { it.copy(amountError = "Invalid amount") }
            return
        }

        if (amount < 100) {
            _uiState.update { it.copy(amountError = "Minimum amount is ₦100") }
            return
        }

        if (amount > group.targetAmount) {
            _uiState.update {
                it.copy(amountError = "Amount cannot exceed target (₦${group.targetAmount})")
            }
            return
        }

        viewModelScope.launch {
            initiatePaymentUseCase(groupId, amount).collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }

                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = null,
                                paymentInitiated = true
                            )
                        }
                        _paymentInitiation.value = result.data
                        Timber.d("Payment initiated: ${result.data.reference}")
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        Timber.e("Payment initiation failed: ${result.message}")
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}