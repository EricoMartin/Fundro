package com.basebox.fundro.ui.components.notification

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.basebox.fundro.core.notification.model.FundroNotification

/**
 * NotificationEntity dialog
 * Shown when app is in foreground and notification received
 */
@Composable
fun FundroNotificationDialog(
    notification: FundroNotification,
    onDismiss: () -> Unit,
    onAction: (FundroNotification) -> Unit,
    modifier: Modifier = Modifier
) {
    val config = NotificationDialogConfig.from(notification)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        AnimatedNotificationDialog(
            notification = notification,
            config = config,
            onDismiss = onDismiss,
            onAction = onAction,
            modifier = modifier
        )
    }
}





