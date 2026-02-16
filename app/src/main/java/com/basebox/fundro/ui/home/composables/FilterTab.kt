package com.basebox.fundro.ui.home.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.basebox.fundro.ui.home.HomeTab

@Composable
fun FilterTabs(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        edgePadding = 16.dp,
        indicator = { },
        divider = { }
    ) {
        HomeTab.values().forEach { tab ->
            val isSelected = selectedTab == tab

            FilterChip(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                label = {
                    Text(
                        text = when (tab) {
                            HomeTab.ALL -> "All"
                            HomeTab.OWNED -> "Owned"
                            HomeTab.PARTICIPATING -> "Participating"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurface
                ),
                border = if (isSelected) null else FilterChipDefaults.filterChipBorder(
                    borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    enabled = TODO(),
                    selected = TODO(),
                    selectedBorderColor = TODO(),
                    disabledBorderColor = TODO(),
                    disabledSelectedBorderColor = TODO(),
                    borderWidth = TODO(),
                    selectedBorderWidth = TODO()
                )
            )
        }
    }
}