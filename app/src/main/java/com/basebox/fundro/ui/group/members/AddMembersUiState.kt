package com.basebox.fundro.ui.group.members

import com.basebox.fundro.domain.model.SearchUser

data class AddMembersUiState(
    val searchQuery: String = "",
    val searchResults: List<SearchUser> = emptyList(),
    val selectedUsers: List<SearchUser> = emptyList(),
    val expectedAmount: String = "",
    val expectedAmountError: String? = null,
    val isSearching: Boolean = false,
    val isAdding: Boolean = false,
    val searchError: String? = null,
    val addError: String? = null,
    val hasSearched: Boolean = false
)