package com.basebox.fundro.core.util

import java.text.NumberFormat
import java.util.*

object CurrencyFormatter {

    /**
     * Format amount as Nigerian Naira
     */
    fun formatNaira(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("en", "NG"))
        format.maximumFractionDigits = 0
        return format.format(amount).replace("NGN", "₦")
    }

    /**
     * Format amount with decimal places
     */
    fun formatNairaWithDecimals(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("en", "NG"))
        format.maximumFractionDigits = 2
        format.minimumFractionDigits = 2
        return format.format(amount).replace("NGN", "₦")
    }

    /**
     * Format large amounts with K, M notation
     */
    fun formatCompact(amount: Double): String {
        return when {
            amount >= 1_000_000 -> "₦${String.format("%.1f", amount / 1_000_000)}M"
            amount >= 1_000 -> "₦${String.format("%.1f", amount / 1_000)}K"
            else -> formatNaira(amount)
        }
    }

    /**
     * Parse currency string to Double
     */
    fun parseNaira(currencyString: String): Double? {
        return try {
            currencyString
                .replace("₦", "")
                .replace(",", "")
                .trim()
                .toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * Extension function for easy formatting
 */
fun Double.toNaira(): String = CurrencyFormatter.formatNaira(this)
fun Double.toNairaWithDecimals(): String = CurrencyFormatter.formatNairaWithDecimals(this)
fun Double.toCompactNaira(): String = CurrencyFormatter.formatCompact(this)