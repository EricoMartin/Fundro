package com.basebox.fundro.core.payment

import android.app.Activity
import co.paystack.android.Paystack
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.basebox.fundro.BuildConfig
import timber.log.Timber

/**
 * Helper class for Paystack SDK integration
 * Handles payment flow with Paystack
 */
object PaystackHelper {

    /**
     * Initialize Paystack SDK
     * Call this in Application onCreate or before first use
     */
    fun initialize(publicKey: String = BuildConfig.PAYSTACK_PUBLIC_KEY) {
        PaystackSdk.setPublicKey(publicKey)
        Timber.d("Paystack SDK initialized")
    }

    /**
     * Charge card using Paystack SDK
     *
     * @param activity The calling activity
     * @param email User's email
     * @param amount Amount in Kobo (multiply Naira by 100)
     * @param accessCode Access code from backend
     * @param callback Payment callback
     */
    fun chargeCard(
        activity: Activity,
        email: String,
        amount: Int,
        accessCode: String,
        callback: (PaymentResult) -> Unit
    ) {
        try {
            val charge = Charge().apply {
                this.card = null // Will be collected via SDK UI
                this.email = email
                this.amount = amount
                this.accessCode = accessCode
            }

            PaystackSdk.chargeCard(activity, charge, object : Paystack.TransactionCallback {
                override fun onSuccess(transaction: Transaction) {
                    Timber.d("Payment successful: ${transaction.reference}")
                    callback(PaymentResult.Success(transaction.reference))
                }

                override fun beforeValidate(transaction: Transaction) {
                    Timber.d("Payment validation started: ${transaction.reference}")
                }

                override fun onError(error: Throwable, transaction: Transaction) {
                    Timber.e(error, "Payment error: ${error.message}")
                    callback(PaymentResult.Error(error.message ?: "Payment failed"))
                }
            })
        } catch (e: Exception) {
            Timber.e(e, "Failed to charge card")
            callback(PaymentResult.Error(e.message ?: "Failed to initialize payment"))
        }
    }

    /**
     * Charge with card details
     * Use this if you want to collect card details yourself
     */
    fun chargeWithCardDetails(
        activity: Activity,
        email: String,
        amount: Int,
        accessCode: String,
        cardNumber: String,
        expiryMonth: Int,
        expiryYear: Int,
        cvv: String,
        callback: (PaymentResult) -> Unit
    ) {
        try {
            val card = Card.Builder(cardNumber, expiryMonth, expiryYear, cvv).build()

            if (!card.validNumber()) {
                callback(PaymentResult.Error("Invalid card number"))
                return
            }

            if (!card.validCVC()) {
                callback(PaymentResult.Error("Invalid CVV"))
                return
            }

            if (!card.validExpiryDate()) {
                callback(PaymentResult.Error("Invalid expiry date"))
                return
            }

            val charge = Charge().apply {
                this.card = card
                this.email = email
                this.amount = amount
                this.accessCode = accessCode
            }

            PaystackSdk.chargeCard(activity, charge, object : Paystack.TransactionCallback {
                override fun onSuccess(transaction: Transaction) {
                    callback(PaymentResult.Success(transaction.reference))
                }

                override fun beforeValidate(transaction: Transaction) {
                    Timber.d("Validating transaction: ${transaction.reference}")
                }

                override fun onError(error: Throwable, transaction: Transaction) {
                    callback(PaymentResult.Error(error.message ?: "Payment failed"))
                }
            })
        } catch (e: Exception) {
            callback(PaymentResult.Error(e.message ?: "Failed to process payment"))
        }
    }
}

/**
 * Payment result sealed class
 */
sealed class PaymentResult {
    data class Success(val reference: String) : PaymentResult()
    data class Error(val message: String) : PaymentResult()
    object Cancelled : PaymentResult()
}