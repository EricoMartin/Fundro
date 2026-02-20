package com.basebox.fundro.core.notification.model

enum class NotificationType(val value: String) {
    // Payment notifications
    PAYMENT_RECEIVED("PAYMENT_RECEIVED"),
    PAYMENT_CONFIRMED("PAYMENT_CONFIRMED"),
    PAYMENT_FAILED("PAYMENT_FAILED"),

    // GroupEntity notifications
    GROUP_CREATED("GROUP_CREATED"),
    GROUP_FUNDED("GROUP_FUNDED"),
    GROUP_RELEASED("GROUP_RELEASED"),
    GROUP_CANCELLED("GROUP_CANCELLED"),

    // Member notifications
    MEMBER_INVITED("MEMBER_INVITED"),
    MEMBER_JOINED("MEMBER_JOINED"),
    MEMBER_PAID("MEMBER_PAID"),

    // KYC notifications
    KYC_APPROVED("KYC_APPROVED"),
    KYC_REJECTED("KYC_REJECTED"),

    // General
    GENERAL("GENERAL");

    companion object {
        fun fromValue(value: String): NotificationType {
            return values().find { it.value == value } ?: GENERAL
        }
    }
}