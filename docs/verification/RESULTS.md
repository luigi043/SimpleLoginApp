# Verification results

Verification date: November 5, 2026 (Europe/Lisbon).

## Published artifacts

- Public interactive demo: <https://quantum-login-demo.vercel.app>
- Installable debug APK: <https://quantum-login-demo.vercel.app/quantum-login-demo.apk>
- Review branch: <https://github.com/luigi043/SimpleLoginApp/tree/improve/login-demo>
- Native and browser screen evidence: [screen index](SCREEN-INDEX.md)

The published web/APK deployment is built from commit `c2c1936456e0b6cc64131f69c947a6f760925a69`. Its Vercel deployment ID is `dpl_2FdtDSxRjAK2Z3kzhNYHaHFKLZCX`. Later commits add test evidence and documentation without changing the deployed app assets.

APK SHA-256:

```text
13fce0db24ebf7d1835d31318ebe771e0f949e3e201ef669b7e6e3c13e92c6e1
```

The APK is 7,842,215 bytes, signed with an Android Debug certificate. `apksigner verify` succeeds. `aapt dump badging` confirms package `com.example.simpleloginapp`, version 1.0, minimum SDK 24, target SDK 34, and compile SDK 35. This is a development artifact, not a store-signed release.

## Build and automated checks

| Check | Result |
| --- | --- |
| Android debug app and instrumentation APK assembly | Passed |
| Kotlin/JUnit credential policy regressions | 3 passed |
| Native emulator walkthroughs on the final APK | 6 passed |
| Android lint | 0 errors, 29 warnings |
| Browser auth/session unit tests | 4 passed |
| Local desktop/mobile browser walkthroughs | 8 passed |
| Anonymous public desktop/mobile browser checks | 10 passed |

Public checks include required fields, malformed email, short password, incorrect credentials, sample login, password visibility, keyboard submission, refresh restoration, cancel/confirm logout, Escape dismissal, JavaScript-disabled behavior, and unavailable tab storage. HTTP checks verified the Content Security Policy, `nosniff`, successful anonymous access, and a SHA-256 match between the downloaded APK and the local tested artifact. No authentication bypass token was used.

See [public browser output](public-browser-tests.txt) and [native instrumentation output](native-tests.txt) for exact execution evidence.

## Environment and native scope

- Windows host; existing JBR 21, Gradle 8.13, AGP 8.13.2, Kotlin 2.0.21.
- Existing Android 15/API 35 x86_64 emulator, serial `emulator-5554`; no physical phone used.
- Native portrait capture size 1080 x 2340; landscape captures rotate that display.
- Native suite uses only `demo@example.com` and synthetic invalid values.
- Existing Chromium reused for browser tests with Playwright 1.64.1.
- Browser viewports: 1440 x 1100 and 390 x 844. Chromium viewport emulation is not a real mobile-browser device test.

The emulator initially displayed a System UI ANR dialog that stole window focus. Earlier runs containing that interruption are not treated as successful final verification. The original functional run passed five cases before the keyboard case lost focus. The dark-theme contrast correction was rebuilt, and the unchanged final APK passed all six direct-instrumentation tests after dismissing the stale emulator System UI dialog. The successful run took 54.793 seconds and produced all ten native captures. No phone data, real credentials, hardware integrations, or private accounts were accessed.

## Remaining limitations

The application has no registration or remote authentication to test. It makes no network requests and requires no camera, Bluetooth, RFID, hardware device, API key, or external account. The only external dependencies are build/package downloads and public static hosting.

Local demo state is editable by the device or browser owner and cannot authorize access to sensitive information. Public sample credentials are intentionally compiled into the app and shown in the UI. The browser stores only tab-scoped metadata; Android stores private demo metadata until logout or app data removal. These differ intentionally and are documented in the setup guide and demo UI.

Android API 24 compatibility was checked with lint, not a second API 24 emulator. Testing covers one API 35 emulator and Chromium at two viewport sizes. A real-device, TalkBack, large-font, cross-browser, Play Store, performance, penetration, and production-authentication review have not been performed.

Lint warnings remain for newer SDK/dependency versions, unused visual resources, a root background overdraw advisory, and optional KTX style suggestions. They are not hidden or converted into a clean-lint claim. Dependency upgrades and a higher Android target require a separate compatibility pass.

Changes are published on `improve/login-demo`; the default `master` branch is unchanged. No force push, history rewrite, or removal of required licenses/attribution was performed. Current tracked project content and the working branch use neutral project naming.
