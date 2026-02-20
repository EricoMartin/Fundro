package com.basebox.fundro.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "full_name")
    val fullName: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,

    @ColumnInfo(name = "role")
    val role: String,

    @ColumnInfo(name = "kyc_status")
    val kycStatus: String,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean,

    @ColumnInfo(name = "bank_name")
    val bankName: String? = null,

    @ColumnInfo(name = "account_holder_name")
    val accountHolderName: String? = null ,

    @ColumnInfo(name = "created_at")
    val createdAt: String? = null,

    @ColumnInfo(name = "bvn")
    val bvn: String? = null,

    @ColumnInfo(name = "bank_account_number")
    val bankAccountNumber: String? = null,

    @ColumnInfo(name = "bank_code")
    val bankCode: String? = null,

    @ColumnInfo(name = "paystack_recipient_code")
    val paystackRecipientCode: String? =  null,

    @ColumnInfo(name = "kyc_rejection_reason")
    val kycRejectionReason: String? = null,

    @ColumnInfo(name = "kyc_verified_at")
    val kycVerifiedAt: String? = null,

    @ColumnInfo(name = "synced_at")
    val syncedAt: Long = System.currentTimeMillis()
)