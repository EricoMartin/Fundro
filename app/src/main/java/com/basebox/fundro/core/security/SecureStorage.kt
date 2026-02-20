package com.basebox.fundro.core.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * CRITICAL: Secure storage for JWT tokens and sensitive data
 * Uses Android Keystore for encryption
 */
@Singleton
class SecureStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs = EncryptedSharedPreferences.create(
        context,
        "fundro_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_FCM_TOKEN = "fcm_token"
    }

    // Access Token (JWT)
    fun saveAccessToken(token: String) {
        encryptedPrefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
        Timber.d("Access token saved securely")
    }

    fun getAccessToken(): String? {
        return encryptedPrefs.getString(KEY_ACCESS_TOKEN, null)
    }

    fun clearAccessToken() {
        encryptedPrefs.edit().remove(KEY_ACCESS_TOKEN).apply()
        Timber.d("Access token cleared")
    }

    // UserEntity ID
    fun saveUserId(userId: String) {
        encryptedPrefs.edit().putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): String? {
        return encryptedPrefs.getString(KEY_USER_ID, null)
    }

    // UserEntity Email
    fun saveUserEmail(email: String) {
        encryptedPrefs.edit().putString(KEY_USER_EMAIL, email).apply()
    }

    fun getUserEmail(): String? {
        return encryptedPrefs.getString(KEY_USER_EMAIL, null)
    }

    // Onboarding Status
    fun setOnboardingCompleted(completed: Boolean) {
        encryptedPrefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }

    fun isOnboardingCompleted(): Boolean {
        return encryptedPrefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    // FCM Token
    fun saveFcmToken(token: String) {
        encryptedPrefs.edit().putString(KEY_FCM_TOKEN, token).apply()
        Timber.d("FCM token saved")
    }

    fun getFcmToken(): String? = encryptedPrefs.getString(KEY_FCM_TOKEN, null)

    fun clearFcmToken() {
        encryptedPrefs.edit().remove(KEY_FCM_TOKEN).apply()
    }

    // Clear all data (logout)
    fun clearAll() {
        encryptedPrefs.edit().clear().apply()
        Timber.d("All secure data cleared")
    }

    // Check if user is logged in
    fun isLoggedIn(): Boolean {
        return !getAccessToken().isNullOrEmpty()
    }
}