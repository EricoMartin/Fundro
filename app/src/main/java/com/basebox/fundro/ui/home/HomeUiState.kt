package com.basebox.fundro.ui.home

import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.domain.model.User

data class HomeUiState(
    val user: User? = null,
    val myGroups: List<Group> = emptyList(),
    val participatingGroups: List<Group> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val invitedGroups: List<Group> = emptyList(),
    val selectedTab: HomeTab = HomeTab.ALL
)

enum class HomeTab {
    ALL,
    OWNED,
    PARTICIPATING,
    INVITATIONS
}