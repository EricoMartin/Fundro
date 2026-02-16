package com.basebox.fundro.core.util

import android.util.Patterns
import java.util.regex.Pattern


// Email validation
fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

// Phone number validation (international format)
fun String.isValidPhoneNumber(): Boolean {
    return Pattern.matches(Constants.PHONE_NUMBER_REGEX, this)
}

// Password validation
fun String.isValidPassword(): Boolean {
    if (length < Constants.MIN_PASSWORD_LENGTH) return false

    val hasUppercase = any { it.isUpperCase() }
    val hasLowercase = any { it.isLowerCase() }
    val hasDigit = any { it.isDigit() }
    val hasSpecial = any { !it.isLetterOrDigit() }

    return hasUppercase && hasLowercase && hasDigit && hasSpecial
}

// Username validation
fun String.isValidUsername(): Boolean {
    if (length !in Constants.USERNAME_MIN_LENGTH..Constants.USERNAME_MAX_LENGTH) return false
    return matches(Regex("^[a-zA-Z0-9_]+$"))
}