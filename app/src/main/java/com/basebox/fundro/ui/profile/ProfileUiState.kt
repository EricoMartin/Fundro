package com.basebox.fundro.ui.profile

import com.basebox.fundro.domain.model.User

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isLoggingOut: Boolean = false,
    val error: String? = null,
    val showLogoutDialog: Boolean = false
)