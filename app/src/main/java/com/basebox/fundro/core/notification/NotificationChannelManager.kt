package com.basebox.fundro.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationChannelManager @Inject constructor(
    private val context: Context
) {
    companion object {
        const val CHANNEL_PAYMENTS = "fundro_payments"
        const val CHANNEL_GROUPS = "fundro_groups"
        const val CHANNEL_MEMBERS = "fundro_members"
        const val CHANNEL_KYC = "fundro_kyc"
        const val CHANNEL_GENERAL = "fundro_general"
    }

    fun createAllChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_PAYMENTS,
                    "Payment Updates",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Real-time payment confirmations and alerts"
                    enableVibration(true)
                    enableLights(true)
                    lightColor = android.graphics.Color.parseColor("#2E5BCC")
                },

                NotificationChannel(
                    CHANNEL_GROUPS,
                    "Group Activity",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Group status updates and milestones"
                    enableVibration(true)
                },

                NotificationChannel(
                    CHANNEL_MEMBERS,
                    "Member Updates",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Member invitations and join notifications"
                },

                NotificationChannel(
                    CHANNEL_KYC,
                    "KYC Verification",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Identity verification status updates"
                    enableVibration(true)
                },

                NotificationChannel(
                    CHANNEL_GENERAL,
                    "General",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "General app notifications"
                }
            )

            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            )

            channels.forEach { channel ->
                notificationManager?.createNotificationChannel(channel)
                Timber.d("Created notification channel: ${channel.id}")
            }
        }
    }

    fun getChannelForType(type: com.basebox.fundro.core.notification.model.NotificationType): String {
        return when (type) {
            com.basebox.fundro.core.notification.model.NotificationType.PAYMENT_RECEIVED,
            com.basebox.fundro.core.notification.model.NotificationType.PAYMENT_CONFIRMED,
            com.basebox.fundro.core.notification.model.NotificationType.PAYMENT_FAILED -> CHANNEL_PAYMENTS

            com.basebox.fundro.core.notification.model.NotificationType.GROUP_CREATED,
            com.basebox.fundro.core.notification.model.NotificationType.GROUP_FUNDED,
            com.basebox.fundro.core.notification.model.NotificationType.GROUP_RELEASED,
            com.basebox.fundro.core.notification.model.NotificationType.GROUP_CANCELLED -> CHANNEL_GROUPS

            com.basebox.fundro.core.notification.model.NotificationType.MEMBER_INVITED,
            com.basebox.fundro.core.notification.model.NotificationType.MEMBER_JOINED,
            com.basebox.fundro.core.notification.model.NotificationType.MEMBER_PAID -> CHANNEL_MEMBERS

            com.basebox.fundro.core.notification.model.NotificationType.KYC_APPROVED,
            com.basebox.fundro.core.notification.model.NotificationType.KYC_REJECTED -> CHANNEL_KYC

            else -> CHANNEL_GENERAL
        }
    }
}