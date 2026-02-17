package com.basebox.fundro.ui.group.members

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.SearchUser
import com.basebox.fundro.domain.usecase.AddMembersToGroupUseCase
import com.basebox.fundro.domain.usecase.SearchUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddMembersViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase,
    private val addMembersToGroupUseCase: AddMembersToGroupUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val groupId: String = checkNotNull(savedStateHandle["groupId"])

    private val _uiState = MutableStateFlow(AddMembersUiState())
    val uiState: StateFlow<AddMembersUiState> = _uiState.asStateFlow()

    private val _addSuccess = MutableStateFlow(false)
    val addSuccess: StateFlow<Boolean> = _addSuccess.asStateFlow()

    private var currentPage = 0
    private var hasMorePages = true

    private var searchJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query, searchError = null) }

        // Cancel previous search
        searchJob?.cancel()

        // Debounce search
        if (query.length >= 2) {
            searchJob = viewModelScope.launch {
                delay(500) // Wait 500ms after user stops typing
                searchUsers(query)
            }
        } else {
            _uiState.update {
                it.copy(
                    searchResults = emptyList(),
                    hasSearched = false
                )
            }
        }
    }

    private fun searchUsers(query: String) {
        viewModelScope.launch {
            searchUsersUseCase(query).collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update { it.copy(isSearching = true, searchError = null) }
                    }

                    is ApiResult.Success -> {
                        val filteredResults = result.data.filter { searchUser ->
                            // Don't show already selected users
                            !_uiState.value.selectedUsers.any { it.id == searchUser.id }
                        }

                        _uiState.update {
                            it.copy(
                                searchResults = filteredResults,
                                isSearching = false,
                                hasSearched = true,
                                searchError = null
                            )
                        }
                        Timber.d("Found ${filteredResults.size} users")
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                searchResults = emptyList(),
                                isSearching = false,
                                hasSearched = true,
                                searchError = result.message
                            )
                        }
                        Timber.e("Search failed: ${result.message}")
                    }
                }
            }
        }
    }

    fun toggleUserSelection(user: SearchUser) {
        val currentSelected = _uiState.value.selectedUsers

        if (currentSelected.any { it.id == user.id }) {
            // Remove from selection
            _uiState.update {
                it.copy(
                    selectedUsers = currentSelected.filter { selectedUser -> selectedUser.id != user.id }
                )
            }
        } else {
            // Add to selection and remove from search results
            _uiState.update {
                it.copy(
                    selectedUsers = currentSelected + user,
                    searchResults = it.searchResults.filter { searchUser -> searchUser.id != user.id }
                )
            }
        }
    }

    fun removeSelectedUser(user: SearchUser) {
        _uiState.update {
            it.copy(
                selectedUsers = it.selectedUsers.filter { selectedUser -> selectedUser.id != user.id }
            )
        }

        // Re-add to search results if there's an active search
        if (_uiState.value.searchQuery.length >= 2) {
            _uiState.update {
                it.copy(
                    searchResults = (it.searchResults + user).sortedBy { user -> user.fullName }
                )
            }
        }
    }

    fun loadMoreResults() {
        if (!hasMorePages || uiState.value.isSearching) return

        currentPage++
        viewModelScope.launch {
            searchUsersUseCase(
                query = uiState.value.searchQuery
            ).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                searchResults = it.searchResults + result.data,
                                isSearching = false
                            )
                        }
                        hasMorePages = result.data.isNotEmpty()
                    }
                    is ApiResult.Error -> {
                        val userFriendlyMessage = when {
                            result.message.contains("403") -> "You don't have permission to add members to this group"
                            result.message.contains("404") -> "This group no longer exists"
                            result.message.contains("409") -> "Some users are already members of this group"
                            result.message.contains("Network") -> "Check your internet connection and try again"
                            else -> result.message
                        }
                        _uiState.update {
                            it.copy(
                                isSearching = false,
                                searchError = userFriendlyMessage
                            )

                        }
                    }
                    ApiResult.Loading -> {
                        _uiState.update { it.copy(isSearching = true) }
                    }
                }
            }
        }
    }

    fun onExpectedAmountChanged(value: String) {
        val filtered = value.filter { it.isDigit() || it == '.' }
        _uiState.update { it.copy(expectedAmount = filtered, expectedAmountError = null) }
    }

    fun addMembers() {
        val currentState = _uiState.value

        if (currentState.selectedUsers.isEmpty()) {
            _uiState.update { it.copy(addError = "Please select at least one user") }
            return
        }

        // Validate expected amount if provided
        var expectedAmount: Double? = null
        if (currentState.expectedAmount.isNotBlank()) {
            val amount = currentState.expectedAmount.toDoubleOrNull()
            if (amount == null) {
                _uiState.update { it.copy(expectedAmountError = "Invalid amount") }
                return
            } else if (amount < 100) {
                _uiState.update { it.copy(expectedAmountError = "Minimum amount is ₦100") }
                return
            }
            expectedAmount = amount
        }

        viewModelScope.launch {
            val userIds = currentState.selectedUsers.map { it.id }

            addMembersToGroupUseCase(groupId, userIds, expectedAmount).collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update { it.copy(isAdding = true, addError = null) }
                    }

                    is ApiResult.Success -> {
                        _uiState.update { it.copy(isAdding = false, addError = null) }
                        _addSuccess.value = true
                        Timber.d("Successfully added ${result.data.size} members")
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isAdding = false,
                                addError = result.message
                            )
                        }
                        Timber.e("Failed to add members: ${result.message}")
                    }
                }
            }
        }
    }

    fun clearAddError() {
        _uiState.update { it.copy(addError = null) }
    }

    fun clearSearchError() {
        _uiState.update { it.copy(searchError = null) }
    }
}