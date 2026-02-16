package com.basebox.fundro.domain.model

data class GroupMember(
    val id: String,
    val userId: String,
    val username: String,
    val fullName: String,
    val status: String,  // INVITED, JOINED, PAID, REMOVED
    val expectedAmount: Double?,
    val paidAmount: Double?,
    val invitedAt: String,
    val joinedAt: String?,
    val paidAt: String?
)