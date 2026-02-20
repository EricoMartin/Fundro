package com.basebox.fundro.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "target_amount")
    val targetAmount: Double,

    @ColumnInfo(name = "total_collected")
    val totalCollected: Double,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "visibility")
    val visibility: String,

    @ColumnInfo(name = "category")
    val category: String?,

    @ColumnInfo(name = "deadline")
    val deadline: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "owner_id")
    val ownerId: String,

    @ColumnInfo(name = "owner_username")
    val ownerUsername: String,

    @ColumnInfo(name = "owner_full_name")
    val ownerFullName: String,

    @ColumnInfo(name = "owner_email")
    val ownerEmail: String,

    @ColumnInfo(name = "participant_count")
    val participantCount: Int,

    @ColumnInfo(name = "progress_percentage")
    val progressPercentage: Double,

    @ColumnInfo(name = "has_current_user_contributed")
    val hasCurrentUserContributed: Boolean,

    @ColumnInfo(name = "group_type")
    val groupType: String, // OWNED, PARTICIPATING, INVITED

    @ColumnInfo(name = "synced_at")
    val syncedAt: Long = System.currentTimeMillis()
)