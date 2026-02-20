package com.basebox.fundro.core.notification

import com.basebox.fundro.core.notification.model.FundroNotification
import com.basebox.fundro.core.notification.model.NotificationType
import com.basebox.fundro.core.security.SecureStorage
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class FundroFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var secureStorage: SecureStorage

    @Inject
    lateinit var registerFcmTokenUseCase: com.basebox.fundro.domain.usecase.RegisterFcmTokenUseCase

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Timber.d("FCM message received from: ${remoteMessage.from}")

        try {
            // Extract notification data
            val data = remoteMessage.data
            val notification = remoteMessage.notification

            val type = NotificationType.fromValue(
                data["type"] ?: "GENERAL"
            )

            val title = notification?.title
                ?: data["title"]
                ?: getDefaultTitle(type)

            val message = notification?.body
                ?: data["message"]
                ?: getDefaultMessage(type)

            val groupId = data["groupId"]
            val contributionId = data["contributionId"]

            val fundroNotification = FundroNotification(
                id = data["notificationId"] ?: UUID.randomUUID().toString(),
                type = type,
                title = title,
                message = message,
                groupId = groupId,
                contributionId = contributionId,
                data = data
            )

            // Show system notification
            notificationHelper.showNotification(fundroNotification)

            // Broadcast for in-app handling
            broadcastNotification(fundroNotification)

            Timber.d("NotificationEntity processed: $type - $title")
        } catch (e: Exception) {
            Timber.e(e, "Failed to process FCM message")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Timber.d("FCM token refreshed")

        // Only register if user is logged in
        if (secureStorage.isLoggedIn()) {
            serviceScope.launch {
                registerFcmTokenUseCase(token).collect { result ->
                    when (result) {
                        is com.basebox.fundro.core.network.ApiResult.Success -> {
                            secureStorage.saveFcmToken(token)
                            Timber.d("FCM token registered with backend")
                        }
                        is com.basebox.fundro.core.network.ApiResult.Error -> {
                            Timber.e("Failed to register FCM token: ${result.message}")
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun broadcastNotification(notification: FundroNotification) {
        val intent = android.content.Intent(ACTION_NOTIFICATION_RECEIVED).apply {
            putExtra(EXTRA_NOTIFICATION_TYPE, notification.type.value)
            putExtra(EXTRA_NOTIFICATION_TITLE, notification.title)
            putExtra(EXTRA_NOTIFICATION_MESSAGE, notification.message)
            putExtra(EXTRA_GROUP_ID, notification.groupId)
            putExtra(EXTRA_CONTRIBUTION_ID, notification.contributionId)
        }
        sendBroadcast(intent)
    }

    private fun getDefaultTitle(type: NotificationType): String = when (type) {
        NotificationType.PAYMENT_RECEIVED -> "Payment Received! 💰"
        NotificationType.PAYMENT_CONFIRMED -> "Payment Confirmed ✅"
        NotificationType.PAYMENT_FAILED -> "Payment Failed ❌"
        NotificationType.GROUP_CREATED -> "New GroupEntity Created 🎉"
        NotificationType.GROUP_FUNDED -> "GroupEntity Fully Funded! 🎊"
        NotificationType.GROUP_RELEASED -> "Funds Released! 🚀"
        NotificationType.GROUP_CANCELLED -> "GroupEntity Cancelled"
        NotificationType.MEMBER_INVITED -> "You've Been Invited! 👋"
        NotificationType.MEMBER_JOINED -> "New Member Joined 🎉"
        NotificationType.MEMBER_PAID -> "Member Payment Received 💰"
        NotificationType.KYC_APPROVED -> "KYC Verified! ✅"
        NotificationType.KYC_REJECTED -> "KYC Verification Failed"
        NotificationType.GENERAL -> "Fundro Update"
    }

    private fun getDefaultMessage(type: NotificationType): String = when (type) {
        NotificationType.PAYMENT_RECEIVED -> "A payment has been received in your group"
        NotificationType.PAYMENT_CONFIRMED -> "Your payment has been confirmed successfully"
        NotificationType.PAYMENT_FAILED -> "Your payment could not be processed"
        NotificationType.GROUP_CREATED -> "A new funding group has been created"
        NotificationType.GROUP_FUNDED -> "Your group has reached its funding target"
        NotificationType.GROUP_RELEASED -> "Funds have been released to the recipient"
        NotificationType.GROUP_CANCELLED -> "A group you're in has been cancelled"
        NotificationType.MEMBER_INVITED -> "You've been invited to join a Fundro group"
        NotificationType.MEMBER_JOINED -> "A new member has joined your group"
        NotificationType.MEMBER_PAID -> "A group member has made their contribution"
        NotificationType.KYC_APPROVED -> "Your identity has been verified successfully"
        NotificationType.KYC_REJECTED -> "Your KYC verification was not successful"
        NotificationType.GENERAL -> "You have a new notification from Fundro"
    }

    companion object {
        const val ACTION_NOTIFICATION_RECEIVED = "com.basebox.fundro.NOTIFICATION_RECEIVED"
        const val EXTRA_NOTIFICATION_TYPE = "notification_type"
        const val EXTRA_NOTIFICATION_TITLE = "notification_title"
        const val EXTRA_NOTIFICATION_MESSAGE = "notification_message"
        const val EXTRA_GROUP_ID = "group_id"
        const val EXTRA_CONTRIBUTION_ID = "contribution_id"
    }
}