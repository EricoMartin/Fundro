package com.basebox.fundro

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.basebox.fundro.BuildConfig
import co.paystack.android.PaystackSdk
import com.basebox.fundro.core.notification.NotificationChannelManager
import com.basebox.fundro.core.payment.PaystackHelper
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class FundroApplication : Application() {

    @Inject
    lateinit var notificationChannelManager: NotificationChannelManager

    override fun onCreate() {
        super.onCreate()

        PaystackHelper.initialize(BuildConfig.PAYSTACK_PUBLIC_KEY)


        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Create notification channels (must be done early)
        notificationChannelManager.createAllChannels()

        // Initialize Paystack SDK
        try {
            PaystackSdk.initialize(applicationContext)
            PaystackSdk.setPublicKey(BuildConfig.PAYSTACK_PUBLIC_KEY)
            Timber.d("Paystack initialized with key: ${BuildConfig.PAYSTACK_PUBLIC_KEY.take(10)}...")
        } catch (e: Exception) {
            Timber.e(e, "Failed to initialize Paystack")
        }

        // Log FCM token in debug
        if (BuildConfig.DEBUG) {
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                Timber.d("FCM Token: $token")
            }
        }
        // Create notification channels
        createNotificationChannels()

        Timber.d("Fundro Application initialized")
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    "payment_notifications",
                    "Payment Updates",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications about payment status and confirmations"
                    enableVibration(true)
                    enableLights(true)
                },

                NotificationChannel(
                    "group_notifications",
                    "GroupEntity Activity",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notifications about group activity and member updates"
                },

                NotificationChannel(
                    "general_notifications",
                    "General",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "General app notifications"
                }
            )

            val notificationManager = ContextCompat.getSystemService(
                this,
                NotificationManager::class.java
            )

            channels.forEach { channel ->
                notificationManager?.createNotificationChannel(channel)
            }
        }
    }
}