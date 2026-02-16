package com.basebox.fundro.ui.group.detail.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.basebox.fundro.ui.group.enums.DetailTab

@Composable
fun DetailTabs(
    selectedTab: DetailTab,
    onTabSelected: (DetailTab) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                color = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        DetailTab.values().forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = when (tab) {
                            DetailTab.DETAILS -> "Details"
                            DetailTab.MEMBERS -> "Members"
                            DetailTab.ACTIVITY -> "Activity"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (selectedTab == tab) FontWeight.SemiBold else FontWeight.Normal
                        )
                    )
                }
            )
        }
    }
}