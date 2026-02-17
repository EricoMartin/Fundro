package com.basebox.fundro.ui.group.detail.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.basebox.fundro.core.util.toDeadlineText
import com.basebox.fundro.core.util.toNaira
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.ui.components.ProgressSection
import com.basebox.fundro.ui.components.StatusBadge
import com.basebox.fundro.ui.group.detail.composables.MetadataItem
import com.basebox.fundro.ui.theme.FundroTextSecondary

@Composable
fun GroupSummaryCard(
    group: Group,
    isOwner: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = group.status)

                if (isOwner) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "You're the owner",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Amount Collected
            Text(
                text = group.totalCollected.toNaira(),
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "of ${group.targetAmount.toNaira()} target",
                style = MaterialTheme.typography.bodyLarge,
                color = FundroTextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Bar
            ProgressSection(
                currentAmount = group.totalCollected,
                targetAmount = group.targetAmount,
                progressPercentage = group.progressPercentage,
                hasReachedGoal = group.progressPercentage >= 100.0
            )

            // Description
            group.description?.let { description ->
                Spacer(modifier = Modifier.height(16.dp))

                Divider(color = MaterialTheme.colorScheme.outlineVariant)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Metadata
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetadataItem(
                    icon = Icons.Default.Group,
                    text = "${group.participantCount} contributors"
                )

                group.deadline?.let { deadline ->
                    MetadataItem(
                        icon = Icons.Default.Schedule,
                        text = deadline.toDeadlineText()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            MetadataItem(
                icon = Icons.Default.Person,
                text = "Recipient: ${group.owner.fullName}"
            )
        }
    }
}