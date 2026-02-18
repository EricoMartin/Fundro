package com.basebox.fundro.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.core.notification.model.FundroNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    fun onNotificationReceived(notification: FundroNotification) {
        _uiState.update {
            it.copy(
                notifications = listOf(notification) + it.notifications,
                activeNotification = notification,
                showDialog = true
            )
        }
    }

    fun onBannerNotificationReceived(notification: FundroNotification) {
        _uiState.update {
            it.copy(
                notifications = listOf(notification) + it.notifications,
                activeNotification = notification,
                showDialog = false
            )
        }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showDialog = false, activeNotification = null) }
    }

    fun dismissBanner() {
        _uiState.update { it.copy(activeNotification = null) }
    }

    fun markAllAsRead() {
        _uiState.update {
            it.copy(
                notifications = it.notifications.map { notif -> notif.copy(isRead = true) }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}