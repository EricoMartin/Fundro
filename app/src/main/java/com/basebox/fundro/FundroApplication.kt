package com.basebox.fundro

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat
import co.paystack.android.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FundroApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
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
                    "Group Activity",
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