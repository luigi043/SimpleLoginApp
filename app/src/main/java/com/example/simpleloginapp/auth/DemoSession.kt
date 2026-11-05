package com.example.simpleloginapp.auth

import android.content.Context

/** Stores demo session metadata only, never the password. Excluded from backup. */
class DemoSession(context: Context) {
    private val preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    val isActive: Boolean
        get() = preferences.getBoolean("isLoggedIn", false) &&
            preferences.getString("userEmail", null) == DemoAuth.EMAIL &&
            preferences.getLong("loginTimestamp", 0) > 0

    val startedAt: Long get() = preferences.getLong("loginTimestamp", 0)

    fun start() {
        preferences.edit().clear()
            .putBoolean("isLoggedIn", true)
            .putString("userEmail", DemoAuth.EMAIL)
            .putLong("loginTimestamp", System.currentTimeMillis())
            .apply()
    }

    fun clear() = preferences.edit().clear().apply()
}
