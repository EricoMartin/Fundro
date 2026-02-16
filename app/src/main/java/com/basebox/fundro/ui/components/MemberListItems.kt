package com.basebox.fundro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.basebox.fundro.ui.theme.FundroGreen
import com.basebox.fundro.ui.theme.FundroOrange
import com.basebox.fundro.ui.theme.FundroTextSecondary
import com.basebox.fundro.core.util.toNaira
import com.basebox.fundro.domain.model.GroupMember

@Composable
fun MemberListItem(
    member: GroupMember,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            if (member.status == "PAID") {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Paid",
                    tint = FundroGreen,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = member.fullName.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Member info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = member.fullName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "@${member.username}",
                style = MaterialTheme.typography.bodySmall,
                color = FundroTextSecondary
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Amount and status
        Column(
            horizontalAlignment = Alignment.End
        ) {
            if (member.paidAmount != null && member.paidAmount > 0) {
                Text(
                    text = member.paidAmount.toNaira(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = FundroGreen
                )
            } else if (member.expectedAmount != null) {
                Text(
                    text = member.expectedAmount.toNaira(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = FundroTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Status badge
            MemberStatusBadge(status = member.status)
        }
    }
}

@Composable
fun MemberStatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, displayText) = when (status.uppercase()) {
        "PAID" -> Triple(FundroGreen.copy(alpha = 0.15f), FundroGreen, "Paid")
        "JOINED" -> Triple(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), MaterialTheme.colorScheme.primary, "Joined")
        "INVITED" -> Triple(FundroOrange.copy(alpha = 0.15f), FundroOrange, "Pending")
        "REMOVED" -> Triple(MaterialTheme.colorScheme.error.copy(alpha = 0.15f), MaterialTheme.colorScheme.error, "Removed")
        else -> Triple(MaterialTheme.colorScheme.surfaceVariant, FundroTextSecondary, status)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor
    ) {
        Text(
            text = displayText,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = textColor
        )
    }
}