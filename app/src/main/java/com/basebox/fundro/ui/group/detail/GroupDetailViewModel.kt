package com.basebox.fundro.ui.group.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.core.security.SecureStorage
import com.basebox.fundro.di.NavigationEvent
import com.basebox.fundro.di.NavigationManager
import com.basebox.fundro.domain.usecase.GetGroupDetailsUseCase
import com.basebox.fundro.domain.usecase.GetGroupMembersUseCase
import com.basebox.fundro.ui.group.enums.DetailTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val getGroupDetailsUseCase: GetGroupDetailsUseCase,
    private val getGroupMembersUseCase: GetGroupMembersUseCase,
    private val secureStorage: SecureStorage,
    private val navigationManager: NavigationManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val groupId: String = checkNotNull(savedStateHandle["groupId"])

    private val _uiState = MutableStateFlow(GroupDetailUiState())
    val uiState: StateFlow<GroupDetailUiState> = _uiState.asStateFlow()

    init {
        loadGroupDetails()
        loadGroupMembers()
    }

    fun refreshAfterPayment() {
        loadGroupDetails(isRefreshing = false)
        loadGroupMembers()
    }

    fun loadGroupDetails(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            getGroupDetailsUseCase(groupId).collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = !isRefreshing,
                                isRefreshing = isRefreshing,
                                error = null
                            )
                        }
                    }

                    is ApiResult.Success -> {
                        val currentUserId = secureStorage.getUserId()
                        val isOwner = result.data.owner.id == currentUserId

                        _uiState.update {
                            it.copy(
                                group = result.data,
                                isLoading = false,
                                isRefreshing = false,
                                isOwner = isOwner,
                                error = null
                            )
                        }
                        Timber.d("GroupEntity loaded: ${result.data.name}")
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = result.message
                            )
                        }
                        Timber.e("Failed to load group: ${result.message}")
                    }
                }
            }
        }
    }

    fun navigateToDisbursement() {
        viewModelScope.launch {
            getGroupDetailsUseCase(groupId).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        if (result.data.status == "FUNDED" && result.data.totalCollected >= result.data.targetAmount) {
                            _uiState.update {
                                it.copy(isCompleted = true)
                            }
                            navigationManager.navigate(NavigationEvent.NavigateTo("disbursement/$groupId"))

                        }
                    }

                    is ApiResult.Error -> {
                        Timber.e("Failed to load group: ${result.message}")
                    }

                    is ApiResult.Loading -> {
                        // Loading handled by group
                    }
                }
            }
        }
    }

    fun loadGroupMembers() {
        viewModelScope.launch {
            getGroupMembersUseCase(groupId).collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                    }

                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                members = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                        Timber.d("GroupEntity members loaded: ${result.data.size}")
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun refresh(){
        loadGroupDetails(isRefreshing = true)
        loadGroupMembers()
    }

    fun selectTab(tab: DetailTab) {
        _uiState.update {
            it.copy(selectedTab = tab)
        }
    }

    fun clearError(){
        _uiState.update {
            it.copy(error = null)
        }
    }
}