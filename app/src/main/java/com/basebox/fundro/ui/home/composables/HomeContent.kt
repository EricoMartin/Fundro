package com.basebox.fundro.ui.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.ui.components.EmptyState
import com.basebox.fundro.ui.components.GroupCard
import com.basebox.fundro.ui.home.composables.FilterTabs
import com.basebox.fundro.ui.home.HomeTab
import com.basebox.fundro.ui.home.HomeUiState
import com.basebox.fundro.ui.home.HomeViewModel
import com.basebox.fundro.ui.home.composables.OverviewCard


@Composable
fun HomeContent(
    uiState: HomeUiState,
    viewModel: HomeViewModel,
    onTabSelected: (HomeTab) -> Unit,
    onGroupClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Overview Card
        OverviewCard(
            myGroups = uiState.myGroups,
            participatingGroups = uiState.participatingGroups
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filter Tabs
        FilterTabs(
            selectedTab = uiState.selectedTab,
            myGroupsCount = uiState.myGroups.size,
            participatingCount = uiState.participatingGroups.size,
            invitedCount = uiState.invitedGroups.size,
            onTabSelected = onTabSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Groups List
        val groups = when (uiState.selectedTab) {
            HomeTab.ALL -> (uiState.myGroups + uiState.participatingGroups).distinct()
            HomeTab.OWNED -> uiState.myGroups
            HomeTab.PARTICIPATING -> uiState.participatingGroups
            HomeTab.INVITATIONS -> uiState.invitedGroups
        }

        if (groups.isEmpty()) {
            EmptyState(
                title = when (uiState.selectedTab) {
                    HomeTab.ALL -> "No groups yet"
                    HomeTab.OWNED -> "No owned groups"
                    HomeTab.PARTICIPATING -> "Not participating in any groups"
                    HomeTab.INVITATIONS -> "No pending invitations"
                },
                message = when (uiState.selectedTab) {
                    HomeTab.ALL -> "Create your first group to get started"
                    HomeTab.OWNED -> "You haven't created any groups yet"
                    HomeTab.PARTICIPATING -> "You're not participating in any groups"
                    HomeTab.INVITATIONS -> "You have no pending group invitations"
                }
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = groups,
                    key = { it.id }
                ) { group ->
                    GroupCard(
                        group = group,
                        onClick = { onGroupClick(group.id) },
                        showInviteActions = uiState.selectedTab == HomeTab.INVITATIONS,
                        onAcceptInvite = if (uiState.selectedTab == HomeTab.INVITATIONS) {
                            { viewModel.acceptMembership(group.id, uiState.user?.id!!) }
                        } else null,
                        onDeclineInvite = if (uiState.selectedTab == HomeTab.INVITATIONS) {
                            { viewModel.declineMembership(group.id, uiState.user?.id!!) }
                        } else null
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp)) // FAB clearance
                }
            }
        }
    }
}

@Composable
fun GroupInviteCard(
    group: Group,
    onClick: () -> Unit,
    onAcceptInvite: (() -> Unit)? = null,
    onDeclineInvite: (() -> Unit),
    modifier: Modifier = Modifier,
    showInviteActions: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            if (showInviteActions && onAcceptInvite != null) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onAcceptInvite,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Accept")
                    }

                    OutlinedButton(
                        onClick = onDeclineInvite,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Decline")
                    }
                }
            }
        }
    }
}
         