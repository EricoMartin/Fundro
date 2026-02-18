package com.basebox.fundro.core.notification.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class FundroNotification(
    val id: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val groupId: String? = null,
    val contributionId: String? = null,
    val data: Map<String, String> = emptyMap()
) {
    fun getIcon(): ImageVector = when (type) {
        NotificationType.PAYMENT_RECEIVED,
        NotificationType.PAYMENT_CONFIRMED -> Icons.Default.Payment
        NotificationType.PAYMENT_FAILED -> Icons.Default.GppBad
        NotificationType.GROUP_CREATED -> Icons.Default.Group
        NotificationType.GROUP_FUNDED -> Icons.Default.AccountBalance
        NotificationType.GROUP_RELEASED -> Icons.Default.CheckCircle
        NotificationType.GROUP_CANCELLED -> Icons.Default.Cancel
        NotificationType.MEMBER_INVITED -> Icons.Default.PersonAdd
        NotificationType.MEMBER_JOINED -> Icons.Default.PersonAddAlt
        NotificationType.MEMBER_PAID -> Icons.Default.Paid
        NotificationType.KYC_APPROVED -> Icons.Default.VerifiedUser
        NotificationType.KYC_REJECTED -> Icons.Default.GppBad
        NotificationType.GENERAL -> Icons.Default.Notifications
    }
}