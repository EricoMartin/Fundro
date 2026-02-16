package com.basebox.fundro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.basebox.fundro.ui.theme.FundroGreen
import com.basebox.fundro.ui.theme.FundroTextSecondary
import com.basebox.fundro.domain.model.Group
import java.text.NumberFormat
import java.util.*
import androidx.compose.material.icons.filled.Schedule
import com.basebox.fundro.core.util.toDeadlineText
import com.basebox.fundro.core.util.toNaira

//@Composable
//fun GroupCard(
//    group: Group,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface
//        ),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 2.dp
//        )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            // Header: Name and Status
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = group.name,
//                    style = MaterialTheme.typography.titleMedium.copy(
//                        fontWeight = FontWeight.SemiBold
//                    ),
//                    color = MaterialTheme.colorScheme.onSurface,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis,
//                    modifier = Modifier.weight(1f)
//                )
//
//                Spacer(modifier = Modifier.width(8.dp))
//
//                StatusBadge(status = group.status)
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            // Amount
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = formatCurrency(group.totalCollected),
//                    style = MaterialTheme.typography.headlineSmall.copy(
//                        fontWeight = FontWeight.Bold
//                    ),
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//
//                Spacer(modifier = Modifier.width(4.dp))
//
//                Text(
//                    text = group.status.lowercase().replaceFirstChar { it.uppercase() },
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = FundroTextSecondary
//                )
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Participants
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Group,
//                    contentDescription = "Participants",
//                    modifier = Modifier.size(16.dp),
//                    tint = FundroTextSecondary
//                )
//
//                Spacer(modifier = Modifier.width(4.dp))
//
//                Text(
//                    text = "${group.participantCount} contributors",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = FundroTextSecondary
//                )
//
//                Spacer(modifier = Modifier.width(12.dp))
//
//                Icon(
//                    imageVector = Icons.Default.Person,
//                    contentDescription = "Recipient",
//                    modifier = Modifier.size(16.dp),
//                    tint = FundroTextSecondary
//                )
//
//                Spacer(modifier = Modifier.width(4.dp))
//
//                Text(
//                    text = "Recipient: ${group.owner.fullName}",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = FundroTextSecondary,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            // Progress indicator
//            Column {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    if (group.hasCurrentUserContributed) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .size(8.dp)
//                                    .clip(RoundedCornerShape(4.dp))
//                                    .background(FundroGreen)
//                            )
//
//                            Spacer(modifier = Modifier.width(6.dp))
//
//                            Text(
//                                text = "Target goal reached",
//                                style = MaterialTheme.typography.bodySmall.copy(
//                                    fontWeight = FontWeight.Medium
//                                ),
//                                color = FundroGreen
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                // Progress bar
//                LinearProgressIndicator(
//                    progress = (group.progressPercentage / 100).toFloat().coerceIn(0f, 1f),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(8.dp)
//                        .clip(RoundedCornerShape(4.dp)),
//                    color = when {
//                        group.progressPercentage >= 100 -> FundroGreen
//                        group.progressPercentage >= 50 -> MaterialTheme.colorScheme.primary
//                        else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
//                    },
//                    trackColor = MaterialTheme.colorScheme.surfaceVariant
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Text(
//                    text = "${String.format("%.1f", group.progressPercentage)}% • ${formatCurrency(group.targetAmount)} target",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = FundroTextSecondary
//                )
//            }
//        }
//    }
//}
//
//private fun formatCurrency(amount: Double): String {
//    val format = NumberFormat.getCurrencyInstance(Locale("en", "NG"))
//    format.maximumFractionDigits = 0
//    return format.format(amount).replace("NGN", "₦")
//}
//
//package com.fundro.core.ui.components




@Composable
fun GroupCard(
    group: Group,
    onClick: () -> Unit,
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

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = group.status.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium,
                    color = FundroTextSecondary,
                    modifier = Modifier.alignByBaseline()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

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