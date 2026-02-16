package com.basebox.fundro.domain.model

data class Group(
    val id: String,
    val name: String,
    val description: String?,
    val targetAmount: Double,
    val status: String,
    val visibility: String,
    val category: String?,
    val deadline: String?,
    val createdAt: String,
    val owner: Owner,
    val totalCollected: Double,
    val participantCount: Int,
    val progressPercentage: Double,
    val hasCurrentUserContributed: Boolean
)