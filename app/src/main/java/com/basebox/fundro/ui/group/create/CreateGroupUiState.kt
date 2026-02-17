package com.basebox.fundro.ui.group.create

import java.time.LocalDate

data class CreateGroupUiState(
    // Form fields
    val name: String = "",
    val description: String = "",
    val targetAmount: String = "",
    val deadline: LocalDate? = null,
    val visibility: String = "PRIVATE",
    val category: String? = null,
    val maxMembers: String = "",
    val generateJoinCode: Boolean = false,

    // Validation errors
    val nameError: String? = null,
    val descriptionError: String? = null,
    val targetAmountError: String? = null,
    val deadlineError: String? = null,
    val maxMembersError: String? = null,

    // State
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentStep: Int = 1
)

enum class GroupVisibility(val displayName: String) {
    PRIVATE("Private"),
    PUBLIC("Public"),
    UNLISTED("Unlisted")
}

enum class GroupCategory(val displayName: String) {
    SUBSCRIPTION("Subscription"),
    CAMPAIGN("Campaign"),
    GIFT("Gift"),
    EVENT("Event"),
    GENERAL("General")
}