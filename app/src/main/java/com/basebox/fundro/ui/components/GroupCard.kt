package com.basebox.fundro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.basebox.fundro.core.util.toDeadlineText
import com.basebox.fundro.core.util.toNaira
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.ui.home.HomeViewModel
import com.basebox.fundro.ui.home.composables.GroupInviteCard
import com.basebox.fundro.ui.theme.FundroGreen
import com.basebox.fundro.ui.theme.FundroTextSecondary

@Composable
fun GroupCard(
    group: Group,
    onClick: () -> Unit,
    showInviteActions: Boolean = false,
    onAcceptInvite: (() -> Unit)? = null,
    onDeclineInvite: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Name and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                StatusBadge(status = group.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Amount collected
            Row {
                Text(
                    text = group.totalCollected.toNaira(),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.alignByBaseline()
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = group.status.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium,
                    color = FundroTextSecondary,
                    modifier = Modifier.alignByBaseline()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            //A button that accepts the user invite
            // if the user has been invited to join the group
            if (showInviteActions && onAcceptInvite != null) {
                GroupInviteCard(
                    group = group,
                    onClick = onClick,
                    onAcceptInvite = onAcceptInvite,
                    onDeclineInvite = onDeclineInvite!!,
                    showInviteActions = true
                )
            }

            // Metadata row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Participants
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "Participants",
                    modifier = Modifier.size(16.dp),
                    tint = FundroTextSecondary
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "${group.participantCount} contributors",
                    style = MaterialTheme.typography.bodySmall,
                    color = FundroTextSecondary
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Recipient
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Recipient",
                    modifier = Modifier.size(16.dp),
                    tint = FundroTextSecondary
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Recipient: ${group.owner.fullName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = FundroTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            // Deadline (if exists)
            group.deadline?.let { deadline ->
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Deadline",
                        modifier = Modifier.size(16.dp),
                        tint = FundroTextSecondary
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = deadline.toDeadlineText(),
                        style = MaterialTheme.typography.bodySmall,
                        color = FundroTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Section
            ProgressSection(
                currentAmount = group.totalCollected,
                targetAmount = group.targetAmount,
                progressPercentage = group.progressPercentage,
                hasReachedGoal = group.progressPercentage >= 100.0
            )

            // Contribution status
            if (group.hasCurrentUserContributed) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(FundroGreen)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "You've contributed",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = FundroGreen
                    )
                }
            }
        }
    }
}