package com.basebox.fundro.ui.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.basebox.fundro.ui.components.EmptyState
import com.basebox.fundro.ui.components.GroupCard
import com.basebox.fundro.ui.home.composables.FilterTabs
import com.basebox.fundro.ui.home.HomeTab
import com.basebox.fundro.ui.home.HomeUiState
import com.basebox.fundro.ui.home.composables.OverviewCard


@Composable
fun HomeContent(
    uiState: HomeUiState,
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
            onTabSelected = onTabSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Groups List
        val groups = when (uiState.selectedTab) {
            HomeTab.ALL -> uiState.myGroups + uiState.participatingGroups
            HomeTab.OWNED -> uiState.myGroups
            HomeTab.PARTICIPATING -> uiState.participatingGroups
        }

        if (groups.isEmpty()) {
            EmptyState(
                title = when (uiState.selectedTab) {
                    HomeTab.ALL -> "No groups yet"
                    HomeTab.OWNED -> "No owned groups"
                    HomeTab.PARTICIPATING -> "Not participating in any groups"
                },
                message = when (uiState.selectedTab) {
                    HomeTab.ALL -> "Create your first group to get started"
                    HomeTab.OWNED -> "You haven't created any groups yet"
                    HomeTab.PARTICIPATING -> "You're not participating in any groups"
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
                        onClick = { onGroupClick(group.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp)) // FAB clearance
                }
            }
        }
    }
}