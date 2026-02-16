package com.basebox.fundro.ui.group.detail.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.basebox.fundro.core.util.toDeadlineText
import com.basebox.fundro.core.util.toNaira
import com.basebox.fundro.core.util.toRelativeTime
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.ui.group.detail.DetailInfoCard


@Composable
fun DetailsTab(
    group: Group,
    onViewDetailsClick: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            DetailInfoCard(
                title = "Pool Information",
                items = listOf(
                    "Target Amount" to group.targetAmount.toNaira(),
                    "Total Collected" to group.totalCollected.toNaira(),
                    "Progress" to "${String.format("%.1f", group.progressPercentage)}%",
                    "Status" to group.status.lowercase().replaceFirstChar { it.uppercase() },
                    "Created" to group.createdAt.toRelativeTime()
                )
            )
        }

        group.deadline?.let { deadline ->
            item {
                DetailInfoCard(
                    title = "Deadline",
                    items = listOf(
                        "Expires" to deadline.toDeadlineText()
                    )
                )
            }
        }

        item {
            DetailInfoCard(
                title = "Recipient",
                items = listOf(
                    "Name" to group.owner.fullName,
                    "Username" to "@${group.owner.username}",
                    "Email" to group.owner.email
                )
            )
        }
    }
}