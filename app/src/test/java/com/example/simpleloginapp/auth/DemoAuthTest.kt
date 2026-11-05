package com.example.simpleloginapp.auth

import org.junit.Assert.*
import org.junit.Test

class DemoAuthTest {
    @Test fun acceptsOnlyThePublishedDemoCredentials() {
        assertTrue(DemoAuth.accepts("demo@example.com", "password123"))
        assertTrue(DemoAuth.accepts("  DEMO@example.com  ", "password123"))
        assertFalse(DemoAuth.accepts("other@example.com", "password123"))
        assertFalse(DemoAuth.accepts("demo@example.com", "wrong-password"))
    }

    @Test fun preservesPasswordWhitespaceAndCase() {
        assertFalse(DemoAuth.accepts(DemoAuth.EMAIL, " password123"))
        assertFalse(DemoAuth.accepts(DemoAuth.EMAIL, "password123 "))
        assertFalse(DemoAuth.accepts(DemoAuth.EMAIL, "Password123"))
    }

    @Test fun distinguishesMissingAndMalformedInput() {
        assertEquals(DemoAuth.Error.REQUIRED, DemoAuth.emailError("  "))
        listOf("no-at-sign", "a@", "a@b", "a b@example.com", "a@@example.com").forEach {
            assertEquals(DemoAuth.Error.INVALID_EMAIL, DemoAuth.emailError(it))
        }
        assertNull(DemoAuth.emailError(" demo@example.com "))
        assertEquals(DemoAuth.Error.REQUIRED, DemoAuth.passwordError(""))
        assertEquals(DemoAuth.Error.SHORT_PASSWORD, DemoAuth.passwordError("12345"))
        assertNull(DemoAuth.passwordError("123456"))
    }
}
