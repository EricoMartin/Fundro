package com.basebox.fundro.core.payment

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

/**
 * Tracks payment status with polling
 * Used to check payment verification status
 */
object PaymentStatusTracker {

    /**
     * Poll payment status at intervals
     *
     * @param maxAttempts Maximum number of polling attempts
     * @param delayMillis Delay between attempts
     * @param checkStatus Suspend function to check status
     */
    fun <T> pollStatus(
        maxAttempts: Int = 10,
        delayMillis: Long = 2000L,
        checkStatus: suspend () -> T
    ): Flow<PollingResult<T>> = flow {
        var attempts = 0

        while (attempts < maxAttempts) {
            attempts++

            try {
                emit(PollingResult.Checking(attempts, maxAttempts))
                val result = checkStatus()
                emit(PollingResult.Success(result))
                return@flow
            } catch (e: Exception) {
                Timber.e(e, "Polling attempt $attempts failed")

                if (attempts >= maxAttempts) {
                    emit(PollingResult.Failed("Payment verification timed out"))
                    return@flow
                }

                delay(delayMillis)
            }
        }
    }
}

/**
 * Polling result sealed class
 */
sealed class PollingResult<out T> {
    data class Checking(val attempt: Int, val maxAttempts: Int) : PollingResult<Nothing>()
    data class Success<T>(val data: T) : PollingResult<T>()
    data class Failed(val message: String) : PollingResult<Nothing>()
}