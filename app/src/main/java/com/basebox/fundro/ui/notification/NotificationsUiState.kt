package com.basebox.fundro.ui.notification

import com.basebox.fundro.core.notification.model.FundroNotification

data class NotificationsUiState(
    val notifications: List<FundroNotification> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val activeNotification: FundroNotification? = null,
    val showDialog: Boolean = false
)