package com.basebox.fundro.ui.home.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.basebox.fundro.ui.home.HomeTab

@Composable
fun FilterTabs(
    selectedTab: HomeTab,
    myGroupsCount: Int,
    participatingCount: Int,
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
        HomeTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab

            val count = when (tab) {
                HomeTab.ALL -> myGroupsCount + participatingCount
                HomeTab.OWNED -> myGroupsCount
                HomeTab.PARTICIPATING -> participatingCount
            }

            FilterChip(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (tab) {
                                HomeTab.ALL -> "All"
                                HomeTab.OWNED -> "Owned"
                                HomeTab.PARTICIPATING -> "Participating"
                            },
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        )

                        if (count > 0) {
                            Spacer(modifier = Modifier.width(12.dp))

                            Surface(
                                shape = CircleShape,
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
                                } else {
                                    MaterialTheme.colorScheme.primaryContainer
                                }
                            ) {
                                Text(
                                    text = count.toString(),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    }
                                )
                            }
                        }
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurface
                ),
                border = if (isSelected) null else FilterChipDefaults.filterChipBorder(
                    borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    enabled = false,
                    selected = false,
                )
            )
        }
    }
}