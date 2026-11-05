# Screen index

These captures belong to the updated Quantum Login demo. Native files are unedited PNG screenshots from Android UiAutomation on the existing Android 15/API 35 emulator. Browser files are Playwright captures of the separate web companion, not native screenshots. All entered values are synthetic public sample data.

## Native Android screens and states

| No. | Screen or state | How to reach it | Evidence |
| --- | --- | --- | --- |
| 01 | Login | Launch with no saved session | [Screenshot](screenshots/native/01-login.png) |
| 02 | Required-field errors | Submit both fields empty | [Screenshot](screenshots/native/02-required-fields.png) |
| 03 | Invalid email and short password | Submit `invalid` and `123` | [Screenshot](screenshots/native/03-invalid-input.png) |
| 04 | Credentials mismatch | Submit a valid non-demo email and wrong password | [Screenshot](screenshots/native/04-credentials-mismatch.png) |
| 05 | Demo profile | Submit the public sample credentials | [Screenshot](screenshots/native/05-profile.png) |
| 06 | Logout confirmation dialog | Tap Log out on the profile | [Screenshot](screenshots/native/06-logout-confirmation.png) |
| 07 | Logged-out login | Confirm logout; saved session is cleared | [Screenshot](screenshots/native/07-logged-out.png) |
| 08 | Landscape login, scrolled to controls | Rotate to landscape and scroll | [Screenshot](screenshots/native/08-login-landscape.png) |
| 09 | Landscape profile, scrolled to logout | Log in in landscape and scroll | [Screenshot](screenshots/native/09-profile-landscape.png) |
| 10 | Visible sample password | Enter public sample values and toggle the password eye | [Screenshot](screenshots/native/10-password-visible.png) |

The app has two navigation destinations and one confirmation dialog. There are no hidden registration, recovery, account-settings, camera, Bluetooth, or payment screens in this codebase. Registration and server-authentication testing do not apply. Canceling logout preserves the profile; confirmation clears the session and removes the profile from navigation history. Recreation and relaunch behavior are checked by instrumentation tests.

## Browser companion

Each view is captured at desktop (1440 x 1100) and mobile (390 x 844) viewport sizes. Full-page captures may be taller than the viewport because the page scrolls.

| State | Desktop | Mobile |
| --- | --- | --- |
| Login with public sample fields | [Screenshot](screenshots/web-desktop-login.png) | [Screenshot](screenshots/web-mobile-login.png) |
| Empty required fields | [Screenshot](screenshots/web-desktop-required-fields.png) | [Screenshot](screenshots/web-mobile-required-fields.png) |
| Malformed email and short password | [Screenshot](screenshots/web-desktop-invalid-input.png) | [Screenshot](screenshots/web-mobile-invalid-input.png) |
| Credentials mismatch | [Screenshot](screenshots/web-desktop-credentials-mismatch.png) | [Screenshot](screenshots/web-mobile-credentials-mismatch.png) |
| Profile after refresh | [Screenshot](screenshots/web-desktop-profile.png) | [Screenshot](screenshots/web-mobile-profile.png) |
| Logout confirmation dialog | [Screenshot](screenshots/web-desktop-logout-dialog.png) | [Screenshot](screenshots/web-mobile-logout-dialog.png) |
| Browser storage unavailable | [Screenshot](screenshots/web-desktop-storage-unavailable.png) | [Screenshot](screenshots/web-mobile-storage-unavailable.png) |

Screenshots document visual state, not authentication security. See [results and limitations](RESULTS.md) for exact checks and environment coverage.
