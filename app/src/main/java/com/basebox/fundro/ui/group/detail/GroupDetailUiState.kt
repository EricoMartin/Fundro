package com.basebox.fundro.ui.group.detail

import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.domain.model.GroupMember
import com.basebox.fundro.ui.group.enums.DetailTab

data class GroupDetailUiState(
    val group: Group? = null,
    val members: List<GroupMember> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val selectedTab: DetailTab = DetailTab.DETAILS,
    val isOwner: Boolean = false
)