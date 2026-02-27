package com.basebox.fundro.ui.disbursement

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.usecase.ReleaseFundsUseCase
import com.basebox.fundro.presentation.disbursement.DisbursementUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DisbursementViewModel @Inject constructor(
    private val releaseFundsUseCase: ReleaseFundsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val groupId: String = checkNotNull(savedStateHandle["groupId"])

    private val _uiState = MutableStateFlow(DisbursementUiState())
    val uiState: StateFlow<DisbursementUiState> = _uiState.asStateFlow()

    init {
        releaseFunds()
    }

    private fun releaseFunds() {
        viewModelScope.launch {
            releaseFundsUseCase(groupId).collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update { it.copy(isProcessing = true) }
                        Timber.d("💰 Processing fund release...")
                    }

                    is ApiResult.Success -> {
                        val disbursement = result.data
                        _uiState.update {
                            it.copy(
                                isProcessing = false,
                                isSuccess = true,
                                amount = disbursement.amount,
                                recipientName = disbursement.recipientName,
                                recipientAccount = disbursement.recipientAccount,
                                disbursedAt = disbursement.disbursedAt
                            )
                        }
                        Timber.d("✅ Funds released successfully")
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isProcessing = false,
                                isFailed = true,
                                error = result.message
                            )
                        }
                        Timber.e("❌ Fund release failed: ${result.message}")
                    }
                }
            }
        }
    }
}