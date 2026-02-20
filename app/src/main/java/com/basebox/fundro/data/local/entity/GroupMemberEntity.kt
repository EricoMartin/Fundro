package com.basebox.fundro.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group_members")
data class GroupMemberEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "group_id")
    val groupId: String,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "full_name")
    val fullName: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "expected_amount")
    val expectedAmount: Double?,

    @ColumnInfo(name = "paid_amount")
    val paidAmount: Double?,

    @ColumnInfo(name = "invited_at")
    val invitedAt: String,

    @ColumnInfo(name = "joined_at")
    val joinedAt: String?,

    @ColumnInfo(name = "paid_at")
    val paidAt: String?,

    @ColumnInfo(name = "synced_at")
    val syncedAt: Long = System.currentTimeMillis()
)