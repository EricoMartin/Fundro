package com.basebox.fundro.core.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.basebox.fundro.R
import com.basebox.fundro.core.notification.model.FundroNotification
import com.basebox.fundro.core.notification.model.NotificationType
import com.basebox.fundro.MainActivity
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class NotificationHelper @Inject constructor(
    private val context: Context,
    private val channelManager: NotificationChannelManager
) {
    private val notificationManager = ContextCompat.getSystemService(
        context,
        NotificationManager::class.java
    )

    fun showNotification(notification: FundroNotification) {
        val channel = channelManager.getChannelForType(notification.type)
        val pendingIntent = createPendingIntent(notification)

        val builder = NotificationCompat.Builder(context, channel)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(getPriorityForType(notification.type))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(notification.message)
            )
            .setColor(
                ContextCompat.getColor(context, R.color.fundro_primary)
            )

        // Add action button for payment notifications
        when (notification.type) {
            NotificationType.MEMBER_INVITED -> {
                val groupId = notification.groupId
                if (groupId != null) {
                    builder.addAction(
                        R.drawable.ic_notification,
                        "View Group",
                        createGroupPendingIntent(groupId)
                    )
                }
            }
            NotificationType.PAYMENT_RECEIVED,
            NotificationType.PAYMENT_CONFIRMED -> {
                val contributionId = notification.contributionId
                if (contributionId != null) {
                    builder.addAction(
                        R.drawable.ic_notification,
                        "View Payment",
                        createPaymentPendingIntent(contributionId)
                    )
                }
            }
            else -> { /* No additional actions */ }
        }

        val notificationId = notification.id.hashCode()
        notificationManager?.notify(notificationId, builder.build())
        Timber.d("Notification shown: ${notification.title}")
    }

    fun cancelNotification(notificationId: Int) {
        notificationManager?.cancel(notificationId)
    }

    fun cancelAllNotifications() {
        notificationManager?.cancelAll()
    }

    private fun createPendingIntent(notification: FundroNotification): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("notification_type", notification.type.value)
            putExtra("group_id", notification.groupId)
            putExtra("contribution_id", notification.contributionId)
        }

        return PendingIntent.getActivity(
            context,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createGroupPendingIntent(groupId: String): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "group/$groupId")
        }

        return PendingIntent.getActivity(
            context,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createPaymentPendingIntent(contributionId: String): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "payment/$contributionId/verify")
        }

        return PendingIntent.getActivity(
            context,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getPriorityForType(type: NotificationType): Int {
        return when (type) {
            NotificationType.PAYMENT_RECEIVED,
            NotificationType.PAYMENT_CONFIRMED,
            NotificationType.PAYMENT_FAILED,
            NotificationType.GROUP_RELEASED,
            NotificationType.KYC_APPROVED,
            NotificationType.KYC_REJECTED -> NotificationCompat.PRIORITY_HIGH

            NotificationType.MEMBER_INVITED,
            NotificationType.GROUP_FUNDED -> NotificationCompat.PRIORITY_DEFAULT

            else -> NotificationCompat.PRIORITY_LOW
        }
    }
}