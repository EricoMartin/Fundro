package com.basebox.fundro.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.basebox.fundro.ui.theme.FundroGreen
import com.basebox.fundro.ui.theme.FundroOrange

@Composable
fun KycStatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (icon, backgroundColor, contentColor, label) = when (status.uppercase()) {
        "VERIFIED" -> Quadruple(
            Icons.Default.CheckCircle,
            FundroGreen.copy(alpha = 0.15f),
            FundroGreen,
            "KYC Verified"
        )
        "PENDING" -> Quadruple(
            Icons.Default.HourglassTop,
            FundroOrange.copy(alpha = 0.15f),
            FundroOrange,
            "KYC Pending"
        )
        "REJECTED" -> Quadruple(
            Icons.Default.Cancel,
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.error,
            "KYC Rejected"
        )
        else -> Quadruple(
            Icons.Default.HourglassTop,
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            status
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(14.dp),
                tint = contentColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = contentColor
            )
        }
    }
}

// Helper data class for 4 values
private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)