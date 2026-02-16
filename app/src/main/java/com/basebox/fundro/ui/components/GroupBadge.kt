package com.basebox.fundro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.basebox.fundro.ui.theme.*

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, displayText) = when (status.uppercase()) {
        "OPEN" -> Triple(FundroSecondaryBlue.copy(alpha = 0.15f), FundroSecondaryBlue, "Open")
        "FUNDED" -> Triple(FundroOrange.copy(alpha = 0.15f), FundroOrange, "Pending")
        "RELEASED" -> Triple(FundroGreen.copy(alpha = 0.15f), FundroGreen, "Released")
        "CANCELLED" -> Triple(FundroRed.copy(alpha = 0.15f), FundroRed, "Cancelled")
        else -> Triple(FundroGray.copy(alpha = 0.15f), FundroGray, status)
    }

    Text(
        text = displayText,
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp
        ),
        color = textColor
    )
}