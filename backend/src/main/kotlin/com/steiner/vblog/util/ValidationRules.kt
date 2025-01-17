package com.steiner.vblog.util

import io.ktor.server.plugins.requestvalidation.*
import java.util.regex.Pattern

fun notBlank(value: String?, fieldName: String = "", allowNull: Boolean = false): ValidationResult {
    if (!allowNull && value == null) {
        return ValidationResult.Invalid("$fieldName cannot be null")
    }

    if (allowNull && value == null) {
        return ValidationResult.Valid
    }

    return if (value!!.trim().let { it.isBlank() || it.isEmpty() }) {
        ValidationResult.Invalid("$fieldName cannot be empty")
    } else {
        ValidationResult.Valid
    }
}

fun limitLength(value: String?, minLength: Int = 0, maxLength: Int, fieldName: String = "", allowNull: Boolean = false): ValidationResult {
    if (!allowNull && value == null) {
        return ValidationResult.Invalid("$fieldName cannot be null")
    }

    if (allowNull && value == null) {
        return ValidationResult.Valid
    }

    return if (value!!.length in minLength..maxLength) {
        ValidationResult.Valid
    } else {
        ValidationResult.Invalid("$fieldName too long or too short")
    }
}

fun emailPattern(value: String?, fieldName: String = "email", allowNull: Boolean = false): ValidationResult {
    if (!allowNull && value == null) {
        return ValidationResult.Invalid("$fieldName cannot be null")
    }

    if (allowNull && value == null) {
        return ValidationResult.Valid
    }

    val flag = Pattern.compile(
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    ).matcher(value!!).matches()

    return if (flag) {
        ValidationResult.Valid
    } else {
        ValidationResult.Invalid("$fieldName not match the email pattern")
    }
}

fun validationChain(vararg results: ValidationResult): ValidationResult {
    return results.firstOrNull { result ->
        result is ValidationResult.Invalid
    } ?: ValidationResult.Valid
}