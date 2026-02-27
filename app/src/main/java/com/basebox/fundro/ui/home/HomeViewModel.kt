package com.basebox.fundro.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.di.NavigationEvent
import com.basebox.fundro.di.NavigationManager
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.domain.usecase.AcceptMembershipUseCase
import com.basebox.fundro.domain.usecase.DeclineMembershipUseCase
import com.basebox.fundro.domain.usecase.GetCompletedGroupsUseCase
import com.basebox.fundro.domain.usecase.GetCurrentUserUseCase
import com.basebox.fundro.domain.usecase.GetInvitedGroupsUseCase
import com.basebox.fundro.domain.usecase.GetMyGroupsUseCase
import com.basebox.fundro.domain.usecase.GetUserGroupsUseCase
import com.basebox.fundro.domain.usecase.JoinGroupUseCase
import com.basebox.fundro.domain.usecase.KycVerificationUseCase
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
    private val joinGroupUseCase: JoinGroupUseCase,
    private val acceptMembershipUseCase: AcceptMembershipUseCase,
    private val declineMembershipUseCase: DeclineMembershipUseCase,
    private val getCompletedGroupsUseCase: GetCompletedGroupsUseCase,
    private val getInvitedGroupsUseCase: GetInvitedGroupsUseCase,
    private val kycVerificationUseCase: KycVerificationUseCase,
    private val navigationManager: NavigationManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _logoutSuccess = MutableStateFlow(false)
    val logoutSuccess: StateFlow<Boolean> = _logoutSuccess.asStateFlow()

    init {
        loadUserData()
        loadGroups()
        loadInvitedGroups()
        loadCompletedGroups()
    }

    fun loadUserData() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _uiState.update { it.copy(user = result.data) }
                        Timber.d("UserEntity loaded: ${result.data.email}")
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

    fun loadInvitedGroups() {
        viewModelScope.launch {
            getInvitedGroupsUseCase().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _uiState.update { it.copy(invitedGroups = result.data) }
                    }
                    is ApiResult.Error -> {
                        Timber.e("Failed to load invited groups: ${result.message}")
                    }
                    is ApiResult.Loading -> {}
                }
            }
        }
    }

//    val completedGroups: StateFlow<List<Group>> = _uiState
//        .map { state ->
//            // Get all groups (owned + participating)
//            val allGroups = state.myGroups + state.participatingGroups
//
//            // Filter only completed groups
//            allGroups.filter { group ->
//                group.status.equals("COMPLETED", ignoreCase = true)
//            }
//        }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList()
//        )

    fun loadCompletedGroups() {
        viewModelScope.launch {
            getCompletedGroupsUseCase().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _uiState.update { it.copy(completedGroups = result.data) }
                    }
                    else -> {}
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

    fun acceptMembership(groupId: String, userId: String) {
        viewModelScope.launch {
            acceptMembershipUseCase(groupId, userId).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = null,
                                acceptedMember = result.data) }

                        // Refresh groups to show updated status
                        loadGroups()
                        Timber.d("Accepted membership successfully")
                    }
                    is ApiResult.Error -> {
                        _uiState.update { it.copy(error = result.message) }
                    }
                    is ApiResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                isRefreshing = true,
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

    fun declineMembership(groupId: String, userId: String) {
        viewModelScope.launch {
            declineMembershipUseCase(groupId, userId).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = null,
                                declinedMember = result.data
                            )
                        }
                        // Refresh groups to show updated status
                        loadGroups()
                        Timber.d("Declined membership successfully")
                    }

                    is ApiResult.Error -> {
                        _uiState.update { it.copy(error = result.message) }
                    }

                    is ApiResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                isRefreshing = true,
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

    fun selectTab(tab: HomeTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun refresh() {
        loadGroups(isRefreshing = true)
        loadInvitedGroups()
        selectTab(HomeTab.ALL)
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

    fun navigateToKyc() {
        val user = uiState.value.user

        viewModelScope.launch {
            kycVerificationUseCase(
                user?.bvn ?: "",
                user?.bankAccountNumber ?: "",
                user?.bankCode ?: "",
                user?.accountHolderName ?: ""
            ).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        if (result.data.status == "VERIFIED") {
                            _uiState.update {
                                it.copy(isKycVerified = true)
                            }
                            navigationManager.navigate(NavigationEvent.NavigateTo("create-group"))

                        }
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(isKycVerified = false)
                        }
                        navigationManager.navigate(NavigationEvent.NavigateTo("profile/kyc"))

                        Timber.e("Kyc is not verified: ${result.message}")
                    }

                    is ApiResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                isRefreshing = true,
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }
}