package com.basebox.fundro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.basebox.fundro.ui.theme.*
import com.basebox.fundro.domain.model.GroupMember
import com.basebox.fundro.ui.theme.FundroGray
import com.basebox.fundro.ui.theme.FundroGreen
import com.basebox.fundro.ui.theme.FundroOrange
import com.basebox.fundro.ui.theme.FundroSecondaryBlue
import com.basebox.fundro.ui.theme.FundroTextSecondary
import java.text.NumberFormat
import java.util.*

@Composable
fun MemberItem(
    member: GroupMember,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    when (member.status) {
                        "PAID" -> FundroGreen.copy(alpha = 0.15f)
                        "JOINED" -> FundroSecondaryBlue.copy(alpha = 0.15f)
                        else -> FundroGray.copy(alpha = 0.15f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = member.fullName.firstOrNull()?.uppercase() ?: "?",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = when (member.status) {
                    "PAID" -> FundroGreen
                    "JOINED" -> FundroSecondaryBlue
                    else -> FundroGray
                }
            )
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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when (member.status) {
                        "PAID" -> Icons.Default.Check
                        else -> Icons.Default.Schedule
                    },
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = when (member.status) {
                        "PAID" -> FundroGreen
                        "JOINED" -> FundroOrange
                        else -> FundroGray
                    }
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = when (member.status) {
                        "PAID" -> "Paid"
                        "JOINED" -> "Pending"
                        "INVITED" -> "Invited"
                        else -> member.status
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = FundroTextSecondary
                )
            }
        }

        // Amount
        member.paidAmount?.let { amount ->
            if (amount > 0) {
                Text(
                    text = formatCurrency(amount),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = FundroGreen
                )
            }
        }
    }
}

private fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "NG"))
    format.maximumFractionDigits = 0
    return format.format(amount).replace("NGN", "₦")
}