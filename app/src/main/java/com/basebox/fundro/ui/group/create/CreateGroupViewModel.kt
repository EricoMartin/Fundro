package com.basebox.fundro.ui.group.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.usecase.CreateGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val createGroupUseCase: CreateGroupUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateGroupUiState())
    val uiState: StateFlow<CreateGroupUiState> = _uiState.asStateFlow()

    private val _createSuccess = MutableStateFlow<String?>(null)
    val createSuccess: StateFlow<String?> = _createSuccess.asStateFlow()

    // Form field updates
    fun onNameChanged(value: String) {
        _uiState.update { it.copy(name = value, nameError = null) }
    }

    fun onDescriptionChanged(value: String) {
        _uiState.update { it.copy(description = value, descriptionError = null) }
    }

    fun onTargetAmountChanged(value: String) {
        // Only allow numbers and decimal point
        val filtered = value.filter { it.isDigit() || it == '.' }
        _uiState.update { it.copy(targetAmount = filtered, targetAmountError = null) }
    }

    fun onDeadlineChanged(value: LocalDate?) {
        _uiState.update { it.copy(deadline = value, deadlineError = null) }
    }

    fun onVisibilityChanged(value: String) {
        _uiState.update { it.copy(visibility = value) }
    }

    fun onCategoryChanged(value: String?) {
        _uiState.update { it.copy(category = value) }
    }

    fun onMaxMembersChanged(value: String) {
        val filtered = value.filter { it.isDigit() }
        _uiState.update { it.copy(maxMembers = filtered, maxMembersError = null) }
    }

    fun onGenerateJoinCodeChanged(value: Boolean) {
        _uiState.update { it.copy(generateJoinCode = value) }
    }

    fun nextStep() {
        val currentState = _uiState.value

        when (currentState.currentStep) {
            1 -> {
                if (validateStep1()) {
                    _uiState.update { it.copy(currentStep = 2) }
                }
            }
            2 -> {
                if (validateStep2()) {
                    createGroup()
                }
            }
        }
    }

    fun previousStep() {
        val currentStep = _uiState.value.currentStep
        if (currentStep > 1) {
            _uiState.update { it.copy(currentStep = currentStep - 1) }
        }
    }

    private fun validateStep1(): Boolean {
        val currentState = _uiState.value
        var hasError = false

        // Validate name
        if (currentState.name.isBlank()) {
            _uiState.update { it.copy(nameError = "Group name is required") }
            hasError = true
        } else if (currentState.name.length < 3) {
            _uiState.update { it.copy(nameError = "Name must be at least 3 characters") }
            hasError = true
        } else if (currentState.name.length > 200) {
            _uiState.update { it.copy(nameError = "Name must be less than 200 characters") }
            hasError = true
        }

        // Validate description (optional but if provided must be valid)
        if (currentState.description.length > 1000) {
            _uiState.update { it.copy(descriptionError = "Description must be less than 1000 characters") }
            hasError = true
        }

        // Validate target amount
        if (currentState.targetAmount.isBlank()) {
            _uiState.update { it.copy(targetAmountError = "Target amount is required") }
            hasError = true
        } else {
            val amount = currentState.targetAmount.toDoubleOrNull()
            if (amount == null) {
                _uiState.update { it.copy(targetAmountError = "Invalid amount") }
                hasError = true
            } else if (amount < 100) {
                _uiState.update { it.copy(targetAmountError = "Minimum amount is ₦100") }
                hasError = true
            } else if (amount > 10_000_000) {
                _uiState.update { it.copy(targetAmountError = "Maximum amount is ₦10,000,000") }
                hasError = true
            }
        }

        return !hasError
    }

    private fun validateStep2(): Boolean {
        val currentState = _uiState.value
        var hasError = false

        // Validate deadline (optional but if provided must be in future)
        currentState.deadline?.let { deadline ->
            if (deadline.isBefore(LocalDate.now())) {
                _uiState.update { it.copy(deadlineError = "Deadline must be in the future") }
                hasError = true
            }
        }

        // Validate max members (optional but if provided must be valid)
        if (currentState.maxMembers.isNotBlank()) {
            val maxMembers = currentState.maxMembers.toIntOrNull()
            if (maxMembers == null) {
                _uiState.update { it.copy(maxMembersError = "Invalid number") }
                hasError = true
            } else if (maxMembers < 2) {
                _uiState.update { it.copy(maxMembersError = "Minimum 2 members") }
                hasError = true
            } else if (maxMembers > 100) {
                _uiState.update { it.copy(maxMembersError = "Maximum 100 members") }
                hasError = true
            }
        }

        return !hasError
    }

    private fun createGroup() {
        val currentState = _uiState.value

        viewModelScope.launch {
            val deadlineString = currentState.deadline?.let { date ->
                // Convert LocalDate to ISO string (backend format: "2024-12-31T23:59:59")
                "${date}T23:59:59"
            }

            val maxMembers = if (currentState.maxMembers.isNotBlank()) {
                currentState.maxMembers.toIntOrNull()
            } else null

            createGroupUseCase(
                name = currentState.name.trim(),
                description = currentState.description.trim().ifBlank { null },
                targetAmount = currentState.targetAmount.toDouble(),
                deadline = deadlineString,
                visibility = currentState.visibility,
                category = currentState.category,
                tags = null,
                maxMembers = maxMembers,
                generateJoinCode = currentState.generateJoinCode
            ).collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }

                    is ApiResult.Success -> {
                        _uiState.update { it.copy(isLoading = false, error = null) }
                        _createSuccess.value = result.data.id
                        Timber.d("Group created successfully: ${result.data.name}")
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        Timber.e("Failed to create group: ${result.message}")
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}