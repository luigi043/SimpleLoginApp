package com.example.simpleloginapp

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.simpleloginapp.auth.DemoSession
import org.hamcrest.Matchers.allOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** Synthetic offline walkthrough. Captures actual emulator pixels using UiAutomation. */
@RunWith(AndroidJUnit4::class)
class LoginFlowTest {
    private val instrumentation get() = InstrumentationRegistry.getInstrumentation()
    private val context get() = instrumentation.targetContext

    @Before fun clearSession() { DemoSession(context).clear() }

    private fun capture(name: String) {
        instrumentation.waitForIdleSync()
        Thread.sleep(400) // Let system bars, keyboard dismissal and navigation animations settle.
        val bitmap = checkNotNull(instrumentation.uiAutomation.takeScreenshot())
        val directory = File(context.getExternalFilesDir(null), "verification").apply { mkdirs() }
        File(directory, "$name.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        bitmap.recycle()
    }

    private fun login(email: String = "demo@example.com", password: String = "password123") {
        onView(withId(R.id.etEmail)).perform(scrollTo(), replaceText(email))
        onView(withId(R.id.etPassword)).perform(scrollTo(), replaceText(password))
        closeSoftKeyboard()
        onView(withId(R.id.btnLogin)).perform(scrollTo(), click())
    }

    @Test fun validationRejectsMissingMalformedAndWrongCredentials() {
        ActivityScenario.launch(MainActivity::class.java).use {
            capture("01-login")
            onView(withId(R.id.btnLogin)).perform(scrollTo(), click())
            closeSoftKeyboard()
            onView(withText(R.string.email_required)).check(matches(isDisplayed()))
            onView(withText(R.string.password_required)).check(matches(isDisplayed()))
            capture("02-required-fields")
            login("invalid", "123")
            onView(withText(R.string.invalid_email)).check(matches(isDisplayed()))
            onView(withText(R.string.password_short)).check(matches(isDisplayed()))
            capture("03-invalid-input")
            login("other@example.com", "wrong-password")
            onView(withId(R.id.tvLoginError)).check(matches(isDisplayed()))
            assertFalse(DemoSession(context).isActive)
            capture("04-credentials-mismatch")
        }
    }

    @Test fun sessionSurvivesRecreationAndLogoutClearsIt() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            login(" DEMO@example.com ")
            onView(withId(R.id.tvUserEmail)).check(matches(withText("demo@example.com")))
            assertTrue(DemoSession(context).isActive)
            capture("05-profile")
            scenario.recreate()
            onView(withId(R.id.tvUserEmail)).check(matches(withText("demo@example.com")))
            onView(withId(R.id.btnLogout)).perform(scrollTo(), click())
            onView(withText(R.string.logout_title)).check(matches(isDisplayed()))
            capture("06-logout-confirmation")
            onView(withText(R.string.cancel)).perform(click())
            assertTrue(DemoSession(context).isActive)
            onView(withId(R.id.btnLogout)).perform(scrollTo(), click())
            onView(allOf(withId(android.R.id.button1), withText(R.string.logout_button))).perform(click())
            onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
            assertFalse(DemoSession(context).isActive)
            capture("07-logged-out")
            scenario.recreate()
            onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        }
    }

    @Test fun rejectsLegacyArbitraryEmailSession() {
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).edit()
            .putBoolean("isLoggedIn", true).putString("userEmail", "other@example.com")
            .putLong("loginTimestamp", 1).commit()
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
            assertFalse(DemoSession(context).isActive)
        }
    }

    @Test fun validSessionRestoresOnRelaunchWithoutPasswordStorage() {
        DemoSession(context).start()
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.tvUserEmail)).check(matches(withText("demo@example.com")))
            val values = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).all
            assertEquals(setOf("isLoggedIn", "userEmail", "loginTimestamp"), values.keys)
        }
    }

    @Test fun landscapeScreensRemainScrollable() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE }
            Thread.sleep(800)
            onView(withId(R.id.btnLogin)).perform(scrollTo())
            capture("08-login-landscape")
            login()
            onView(withId(R.id.btnLogout)).perform(scrollTo())
            onView(withId(R.id.btnLogout)).check(matches(isDisplayed()))
            capture("09-profile-landscape")
            scenario.onActivity { it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED }
        }
    }
    @Test fun keyboardLoginPasswordVisibilityAndLogoutBackStack() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.etEmail)).perform(scrollTo(), replaceText("demo@example.com"))
            onView(withId(R.id.etPassword)).perform(scrollTo(), replaceText("password123"))
            closeSoftKeyboard()
            onView(allOf(withId(com.google.android.material.R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.tilPassword)))).perform(click())
            capture("10-password-visible")
            onView(withId(R.id.etPassword)).perform(click(), pressImeActionButton())
            onView(withId(R.id.tvUserEmail)).check(matches(withText("demo@example.com")))
            onView(withId(R.id.btnLogout)).perform(scrollTo(), click())
            onView(withId(android.R.id.button1)).perform(click())
            androidx.test.espresso.Espresso.pressBackUnconditionally()
        }
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
            assertFalse(DemoSession(context).isActive)
        }
    }
}
