package com.basebox.fundro.ui.group.detail.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.basebox.fundro.ui.group.detail.ActionButtonsRow
import com.basebox.fundro.ui.group.detail.ActivityTab
import com.basebox.fundro.ui.group.detail.DetailTabs
import com.basebox.fundro.ui.group.detail.DetailsTab
import com.basebox.fundro.ui.group.detail.GroupDetailUiState
import com.basebox.fundro.ui.group.detail.GroupSummaryCard
import com.basebox.fundro.ui.group.detail.MembersTab
import com.basebox.fundro.ui.group.enums.DetailTab

    @Composable
    fun GroupDetailContent(
        uiState: GroupDetailUiState,
        onTabSelected: (DetailTab) -> Unit,
        onContributeClick: () -> Unit,
        onAddMembersClick: () -> Unit,
        onViewDetailsClick: () -> Unit
    ) {
        val group = uiState.group ?: return

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Group Summary Card
            GroupSummaryCard(
                group = group,
                isOwner = uiState.isOwner
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            ActionButtonsRow(
                group = group,
                isOwner = uiState.isOwner,
                onContributeClick = onContributeClick,
                onAddMembersClick = onAddMembersClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs
            DetailTabs(
                selectedTab = uiState.selectedTab,
                onTabSelected = onTabSelected
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tab Content
            when (uiState.selectedTab) {
                DetailTab.DETAILS -> {
                    DetailsTab(
                        group = group,
                        onViewDetailsClick = onViewDetailsClick
                    )
                }

                DetailTab.MEMBERS -> {
                    MembersTab(
                        members = uiState.members,
                        isOwner = uiState.isOwner,
                        onAddMembersClick = onAddMembersClick
                    )
                }

                DetailTab.ACTIVITY -> {
                    ActivityTab(group = group)
                }
            }
        }
    }