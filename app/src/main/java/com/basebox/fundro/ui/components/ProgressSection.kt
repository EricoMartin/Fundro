package com.basebox.fundro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.basebox.fundro.ui.theme.FundroGreen
import com.basebox.fundro.ui.theme.FundroTextSecondary
import java.text.NumberFormat
import java.util.*

/**
 * COMPLETE Progress Section Component
 * Shows progress bar with percentage and target amount
 */
@Composable
fun ProgressSection(
    currentAmount: Double,
    targetAmount: Double,
    progressPercentage: Double,
    hasReachedGoal: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Top row: Status or Goal reached indicator
        if (hasReachedGoal) {
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(FundroGreen)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Target goal reached",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = FundroGreen
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Progress bar
        LinearProgressIndicator(
            progress = (progressPercentage / 100).toFloat().coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = when {
                progressPercentage >= 100 -> FundroGreen
                progressPercentage >= 50 -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            },
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Bottom row: Percentage and target
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${String.format("%.1f", progressPercentage)}% complete",
                style = MaterialTheme.typography.bodySmall,
                color = FundroTextSecondary
            )

            Text(
                text = "${formatCurrency(currentAmount)} of ${formatCurrency(targetAmount)}",
                style = MaterialTheme.typography.bodySmall,
                color = FundroTextSecondary
            )
        }
    }
}

/**
 * Simplified progress bar without text
 */
@Composable
fun SimpleProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary
) {
    LinearProgressIndicator(
        progress = progress.coerceIn(0f, 1f),
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
        color = color,
        trackColor = MaterialTheme.colorScheme.surfaceVariant
    )
}

private fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "NG"))
    format.maximumFractionDigits = 0
    return format.format(amount).replace("NGN", "₦")
}