package com.basebox.fundro.ui.components.notification

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.basebox.fundro.core.notification.model.FundroNotification
import com.basebox.fundro.core.notification.model.NotificationType
import com.basebox.fundro.ui.theme.FundroGreen
import com.basebox.fundro.ui.theme.FundroOrange
import com.basebox.fundro.ui.theme.FundroTextSecondary
import com.basebox.fundro.ui.theme.*;

@Composable
public fun NotificationDialogContent(
    notification: FundroNotification,
    config: NotificationDialogConfig,
    onDismiss: () -> Unit,
    onAction: (FundroNotification) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = notification.title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Message
        Text(
            text = notification.message,
            style = MaterialTheme.typography.bodyMedium,
            color = FundroTextSecondary,
            textAlign = TextAlign.Center
        )

        // Extra details
        if (notification.groupId != null || notification.contributionId != null) {
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = FundroTextSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when {
                            notification.type == NotificationType.PAYMENT_RECEIVED ||
                                    notification.type == NotificationType.PAYMENT_CONFIRMED ->
                                "Tap to view payment details"
                            notification.groupId != null ->
                                "Tap to view group"
                            else -> "Tap for more details"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = FundroTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action buttons
        if (config.hasAction) {
            Button(
                onClick = {
                    onAction(notification)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = config.actionButtonColor
                )
            ) {
                Text(
                    text = config.actionButtonText,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.5.dp
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    text = "Dismiss",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        } else {
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Got it",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

/**
 * Configuration for each notification type
 */
data class NotificationDialogConfig(
    val icon: ImageVector,
    val iconBackgroundColor: Color,
    val gradientColors: List<Color>,
    val hasAction: Boolean,
    val actionButtonText: String,
    val actionButtonColor: Color
) {
    companion object {
        @Composable
        fun from(notification: FundroNotification): NotificationDialogConfig {
            return when (notification.type) {
                NotificationType.PAYMENT_RECEIVED,
                NotificationType.PAYMENT_CONFIRMED -> NotificationDialogConfig(
                    icon = Icons.Default.CheckCircle,
                    iconBackgroundColor = FundroGreen,
                    gradientColors = listOf(
                        FundroGreen,
                        FundroGreen.copy(alpha = 0.6f)
                    ),
                    hasAction = notification.contributionId != null,
                    actionButtonText = "View Payment",
                    actionButtonColor = FundroGreen
                )

                NotificationType.PAYMENT_FAILED -> NotificationDialogConfig(
                    icon = Icons.Default.Error,
                    iconBackgroundColor = MaterialTheme.colorScheme.error,
                    gradientColors = listOf(
                        MaterialTheme.colorScheme.error,
                        MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                    ),
                    hasAction = true,
                    actionButtonText = "Try Again",
                    actionButtonColor = MaterialTheme.colorScheme.error
                )

                NotificationType.GROUP_FUNDED,
                NotificationType.GROUP_RELEASED -> NotificationDialogConfig(
                    icon = Icons.Default.Celebration,
                    iconBackgroundColor = FundroGreen,
                    gradientColors = listOf(
                        Color(0xFF059669),
                        Color(0xFF10B981)
                    ),
                    hasAction = notification.groupId != null,
                    actionButtonText = "View Group",
                    actionButtonColor = FundroGreen
                )

                NotificationType.GROUP_CANCELLED -> NotificationDialogConfig(
                    icon = Icons.Default.Cancel,
                    iconBackgroundColor = FundroOrange,
                    gradientColors = listOf(
                        FundroOrange,
                        FundroOrange.copy(alpha = 0.6f)
                    ),
                    hasAction = false,
                    actionButtonText = "",
                    actionButtonColor = FundroOrange
                )

                NotificationType.MEMBER_INVITED -> NotificationDialogConfig(
                    icon = Icons.Default.PersonAdd,
                    iconBackgroundColor = FundroPrimaryBlue,
                    gradientColors = listOf(
                        FundroPrimaryBlue,
                        FundroSecondaryBlue
                    ),
                    hasAction = notification.groupId != null,
                    actionButtonText = "View Invitation",
                    actionButtonColor = FundroPrimaryBlue
                )

                NotificationType.MEMBER_PAID,
                NotificationType.MEMBER_JOINED -> NotificationDialogConfig(
                    icon = Icons.Default.Group,
                    iconBackgroundColor = FundroPrimaryBlue,
                    gradientColors = listOf(
                        FundroPrimaryBlue,
                        FundroSecondaryBlue
                    ),
                    hasAction = notification.groupId != null,
                    actionButtonText = "View Group",
                    actionButtonColor = FundroPrimaryBlue
                )

                NotificationType.KYC_APPROVED -> NotificationDialogConfig(
                    icon = Icons.Default.VerifiedUser,
                    iconBackgroundColor = FundroGreen,
                    gradientColors = listOf(
                        FundroGreen,
                        FundroGreen.copy(alpha = 0.7f)
                    ),
                    hasAction = false,
                    actionButtonText = "",
                    actionButtonColor = FundroGreen
                )

                NotificationType.KYC_REJECTED -> NotificationDialogConfig(
                    icon = Icons.Default.GppBad,
                    iconBackgroundColor = MaterialTheme.colorScheme.error,
                    gradientColors = listOf(
                        MaterialTheme.colorScheme.error,
                        MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                    ),
                    hasAction = true,
                    actionButtonText = "Re-submit KYC",
                    actionButtonColor = MaterialTheme.colorScheme.error
                )

                else -> NotificationDialogConfig(
                    icon = Icons.Default.Notifications,
                    iconBackgroundColor = FundroPrimaryBlue,
                    gradientColors = listOf(
                        FundroPrimaryBlue,
                        FundroSecondaryBlue
                    ),
                    hasAction = false,
                    actionButtonText = "",
                    actionButtonColor = FundroPrimaryBlue
                )
            }
        }
    }
}