package com.basebox.fundro.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.usecase.GetCurrentUserUseCase
import com.basebox.fundro.domain.usecase.GetMyGroupsUseCase
import com.basebox.fundro.domain.usecase.GetUserGroupsUseCase
import com.basebox.fundro.domain.usecase.JoinGroupUseCase
import com.basebox.fundro.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserGroupsUseCase: GetUserGroupsUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val joinGroupUseCase: JoinGroupUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _logoutSuccess = MutableStateFlow(false)
    val logoutSuccess: StateFlow<Boolean> = _logoutSuccess.asStateFlow()

    init {
        loadUserData()
        loadGroups()
    }

    fun loadUserData() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _uiState.update { it.copy(user = result.data) }
                        Timber.d("User loaded: ${result.data.email}")
                    }
                    is ApiResult.Error -> {
                        Timber.e("Failed to load user: ${result.message}")
                    }
                    is ApiResult.Loading -> {
                        // Loading handled by groups
                    }
                }
            }
        }
    }

    fun loadGroups(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            getUserGroupsUseCase().collect { result ->
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
                        val (myGroups, participatingGroups) = result.data
                        _uiState.update {
                            it.copy(
                                myGroups = myGroups,
                                participatingGroups = participatingGroups,
                                isLoading = false,
                                isRefreshing = false,
                                error = null
                            )
                        }
                        Timber.d("Loaded ${myGroups.size} owned groups, ${participatingGroups.size} participating")
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = result.message
                            )
                        }
                        Timber.e("Failed to load groups: ${result.message}")
                    }
                }
            }
        }
    }

    fun joinGroup(groupId: String) {
        viewModelScope.launch {
            joinGroupUseCase(groupId).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        // Refresh groups to show updated status
                        loadGroups()
                        Timber.d("Joined group successfully")
                    }
                    is ApiResult.Error -> {
                        _uiState.update { it.copy(error = result.message) }
                    }
                    is ApiResult.Loading -> {}
                }
            }
        }
    }

    fun selectTab(tab: HomeTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun refresh() {
        loadGroups(isRefreshing = true)
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _logoutSuccess.value = true
                        Timber.d("Logout successful")
                    }
                    is ApiResult.Error -> {
                        Timber.e("Logout failed: ${result.message}")
                    }
                    is ApiResult.Loading -> {}
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}