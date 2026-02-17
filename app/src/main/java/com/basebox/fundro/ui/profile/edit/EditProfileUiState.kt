package com.basebox.fundro.ui.profile.edit

data class EditProfileUiState(
    val fullName: String = "",
    val username: String = "",
    val phoneNumber: String = "",
    val fullNameError: String? = null,
    val usernameError: String? = null,
    val phoneError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
)