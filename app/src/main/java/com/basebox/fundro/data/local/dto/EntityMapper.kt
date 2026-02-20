package com.basebox.fundro.data.local.dto

import com.basebox.fundro.data.local.entity.*
import com.basebox.fundro.domain.model.*

// UserEntity mappings
fun User.toEntity() = UserEntity(
    id = id,
    username = username,
    fullName = fullName,
    email = email,
    phoneNumber = phoneNumber,
    role = role,
    kycStatus = kycStatus,
    isActive = isActive,
    bankName = bankName,
    accountHolderName = accountHolderName,
    createdAt = createdAt,
    bvn = bvn,
    bankAccountNumber = bankAccountNumber,
    bankCode = bankCode,
    paystackRecipientCode = paystackRecipientCode,
    kycRejectionReason = kycRejectionReason,
    kycVerifiedAt = kycVerifiedAt,
    syncedAt = System.currentTimeMillis()
)

fun UserEntity.toDomain() = User(
    id = id,
    username = username,
    fullName = fullName,
    email = email,
    phoneNumber = phoneNumber,
    role = role,
    kycStatus = kycStatus,
    isActive = isActive,
    bankName = bankName,
    accountHolderName = accountHolderName,
    createdAt = createdAt!!,
    bvn = bvn,
    bankAccountNumber = bankAccountNumber,
    bankCode = bankCode,
    paystackRecipientCode = paystackRecipientCode!!,
    kycRejectionReason = kycRejectionReason!!,
    kycVerifiedAt = kycVerifiedAt

)

// GroupEntity mappings
fun Group.toEntity(groupType: String) = GroupEntity(
    id = id,
    name = name,
    description = description,
    targetAmount = targetAmount,
    totalCollected = totalCollected,
    status = status,
    visibility = visibility,
    category = category,
    deadline = deadline,
    createdAt = createdAt,
    ownerId = owner.id,
    ownerUsername = owner.username,
    ownerFullName = owner.fullName,
    ownerEmail = owner.email,
    participantCount = participantCount,
    progressPercentage = progressPercentage,
    hasCurrentUserContributed = hasCurrentUserContributed,
    groupType = groupType
)

fun GroupEntity.toDomain() = Group(
    id = id,
    name = name,
    description = description,
    targetAmount = targetAmount,
    totalCollected = totalCollected,
    status = status,
    visibility = visibility,
    category = category,
    deadline = deadline,
    createdAt = createdAt,
    owner = Owner(
        id = ownerId,
        username = ownerUsername,
        fullName = ownerFullName,
        email = ownerEmail
    ),
    participantCount = participantCount,
    progressPercentage = progressPercentage,
    hasCurrentUserContributed = hasCurrentUserContributed
)

// GroupMemberEntity mappings
fun GroupMember.toEntity(groupId: String) = GroupMemberEntity(
    id = id,
    groupId = groupId,
    userId = userId,
    username = username,
    fullName = fullName,
    status = status,
    expectedAmount = expectedAmount,
    paidAmount = paidAmount,
    invitedAt = invitedAt,
    joinedAt = joinedAt,
    paidAt = paidAt
)

fun GroupMemberEntity.toDomain() = GroupMember(
    id = id,
    userId = userId,
    username = username,
    fullName = fullName,
    status = status,
    expectedAmount = expectedAmount,
    paidAmount = paidAmount,
    invitedAt = invitedAt,
    joinedAt = joinedAt,
    paidAt = paidAt
)