package com.example.simpleloginapp.auth

/** Offline demonstration only. These public constants are not server authentication. */
object DemoAuth {
    const val EMAIL = "demo@example.com"
    const val PASSWORD = "password123"

    enum class Error { REQUIRED, INVALID_EMAIL, SHORT_PASSWORD }

    fun normalizeEmail(email: String) = email.trim()

    fun emailError(email: String): Error? = when {
        normalizeEmail(email).isEmpty() -> Error.REQUIRED
        !Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(normalizeEmail(email)) -> Error.INVALID_EMAIL
        else -> null
    }

    fun passwordError(password: String): Error? = when {
        password.isEmpty() -> Error.REQUIRED
        password.length < 6 -> Error.SHORT_PASSWORD
        else -> null
    }

    fun accepts(email: String, password: String) =
        normalizeEmail(email).equals(EMAIL, ignoreCase = true) && password == PASSWORD
}
