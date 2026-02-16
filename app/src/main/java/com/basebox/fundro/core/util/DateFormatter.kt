package com.basebox.fundro.core.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateFormatter {

    private val displayFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
    private val fullFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a")

    /**
     * Format ISO date string to readable format
     */
    fun formatDate(isoString: String): String {
        return try {
            val dateTime = LocalDateTime.parse(isoString.replace("Z", ""))
            dateTime.format(displayFormatter)
        } catch (e: Exception) {
            isoString
        }
    }

    /**
     * Format to relative time (e.g., "2 hours ago", "3 days ago")
     */
    fun formatRelativeTime(isoString: String): String {
        return try {
            val dateTime = LocalDateTime.parse(isoString.replace("Z", ""))
            val now = LocalDateTime.now()

            val minutes = ChronoUnit.MINUTES.between(dateTime, now)
            val hours = ChronoUnit.HOURS.between(dateTime, now)
            val days = ChronoUnit.DAYS.between(dateTime, now)

            when {
                minutes < 1 -> "Just now"
                minutes < 60 -> "$minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
                hours < 24 -> "$hours ${if (hours == 1L) "hour" else "hours"} ago"
                days < 7 -> "$days ${if (days == 1L) "day" else "days"} ago"
                days < 30 -> "${days / 7} ${if (days / 7 == 1L) "week" else "weeks"} ago"
                days < 365 -> "${days / 30} ${if (days / 30 == 1L) "month" else "months"} ago"
                else -> "${days / 365} ${if (days / 365 == 1L) "year" else "years"} ago"
            }
        } catch (e: Exception) {
            isoString
        }
    }

    /**
     * Format deadline (e.g., "Expires in 2 days")
     */
    fun formatDeadline(isoString: String): String {
        return try {
            val deadline = LocalDateTime.parse(isoString.replace("Z", ""))
            val now = LocalDateTime.now()

            val days = ChronoUnit.DAYS.between(now, deadline)
            val hours = ChronoUnit.HOURS.between(now, deadline)

            when {
                days < 0 -> "Expired"
                days == 0L && hours > 0 -> "Expires in $hours ${if (hours == 1L) "hour" else "hours"}"
                days == 0L -> "Expires today"
                days == 1L -> "Expires tomorrow"
                days < 7 -> "Expires in $days days"
                days < 30 -> "Expires in ${days / 7} weeks"
                else -> "Expires in ${days / 30} months"
            }
        } catch (e: Exception) {
            isoString
        }
    }
}

/**
 * Extension functions
 */
fun String.toFormattedDate(): String = DateFormatter.formatDate(this)
fun String.toRelativeTime(): String = DateFormatter.formatRelativeTime(this)
fun String.toDeadlineText(): String = DateFormatter.formatDeadline(this)