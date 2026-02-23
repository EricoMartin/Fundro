package com.basebox.fundro.ui.payment.verify

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.usecase.VerifyPaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PaymentVerificationViewModel @Inject constructor(
    private val verifyPaymentUseCase: VerifyPaymentUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val contributionId: String = checkNotNull(savedStateHandle["contributionId"])



    private val _uiState = MutableStateFlow(PaymentVerificationUiState())
    val uiState: StateFlow<PaymentVerificationUiState> = _uiState.asStateFlow()

    init {
        verifyPayment()
    }

    private fun verifyPayment() {
        viewModelScope.launch {
            var attempts = 0
            val maxAttempts = 10
            val delayBetweenAttempts = 5000L // 5 seconds

            while (attempts < maxAttempts) {
                attempts++

                _uiState.update {
                    it.copy(
                        isVerifying = true,
                        verificationAttempt = attempts,
                        maxAttempts = maxAttempts
                    )
                }

                Timber.d("Contribution ID if not null is: $contributionId")
                verifyPaymentUseCase(contributionId).collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {
                            Timber.d("Verifying payment attempt $attempts")
                        }

                        is ApiResult.Success -> {
                            val verification = result.data

                            when (verification.status.uppercase()) {
                                "COMPLETED" -> {
                                    _uiState.update {
                                        it.copy(
                                            isVerifying = false,
                                            status = verification.status,
                                            amount = verification.amount,
                                            paidAt = verification.paidAt,
                                            isSuccess = true,
                                            isFailed = false
                                        )
                                    }
                                    Timber.d("Payment verified successfully")
                                    return@collect // Exit polling loop
                                }

                                "FAILED", "REFUNDED" -> {
                                    _uiState.update {
                                        it.copy(
                                            isVerifying = false,
                                            status = verification.status,
                                            error = verification.message ?: "Payment failed",
                                            isSuccess = false,
                                            isFailed = true
                                        )
                                    }
                                    Timber.e("Payment failed: ${verification.status}")
                                    return@collect // Exit polling loop
                                }

                                "PENDING" -> {
                                    // Continue polling
                                    Timber.d("Payment still pending, will retry...")
                                }
                            }
                        }

                        is ApiResult.Error -> {
                            Timber.e("Verification attempt $attempts failed: ${result.message}")

                            if (attempts >= maxAttempts) {
                                _uiState.update {
                                    it.copy(
                                        isVerifying = false,
                                        error = "Payment verification timed out. Please check your contributions.",
                                        isFailed = true
                                    )
                                }
                                return@collect
                            }
                        }
                    }
                }

                if (attempts < maxAttempts && !_uiState.value.isSuccess && !_uiState.value.isFailed) {
                    delay(delayBetweenAttempts)
                }
            }

            // If we've exhausted all attempts
            if (!_uiState.value.isSuccess) {
                _uiState.update {
                    it.copy(
                        isVerifying = false,
                        error = "Payment verification timed out. Please check your contributions.",
                        isFailed = true
                    )
                }
            }
        }
    }

    fun retryVerification() {
        _uiState.update {
            PaymentVerificationUiState(isVerifying = true)
        }
        verifyPayment()
    }
}