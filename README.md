# Quantum Login (SimpleLoginApp)

A small Kotlin Android application for exploring an offline login, local session, profile, and logout flow. It is a learning and UI demonstration project. It does not authenticate against a server or create real accounts.

Use only the public sample credentials:

- Email: `demo@example.com`
- Password: `password123`

Email matching ignores case and surrounding whitespace. Passwords must match exactly, including case and whitespace. Any other credentials are rejected. There is no registration, password reset, email verification, subscription, or backend service.

## Try it

The interactive browser companion and installable debug APK are documented in [verification results](docs/verification/RESULTS.md). The browser companion implements the same flow in HTML, CSS, and JavaScript. It is not an Android emulator.

See [the screen index](docs/verification/SCREEN-INDEX.md) for actual native screenshots and separate browser captures. Historical images in `docs/screenshots` predate this verification and are not evidence of the updated build.

## Android setup

Open **this repository root** in Android Studio. The duplicate nested project has been removed because it contained the same app source with a conflicting older build configuration.

Required tooling:

- JDK 17 or a compatible newer JDK. This verification uses the existing JBR 21 installation with Java/Kotlin bytecode target 17.
- Android SDK Platform 35 and accepted SDK licenses.
- The committed Gradle 8.13 wrapper and Android Gradle Plugin 8.13.2.
- Android 7.0 (API 24) or later for installation. Target SDK remains 34.
- Internet access for initial dependencies from Google Maven, Maven Central, and the Gradle distribution service.

Set `JAVA_HOME` and `ANDROID_HOME` to your local installations. Alternatively, set `sdk.dir` in an untracked `local.properties` file. Do not commit machine-specific Java or SDK paths.

On Windows PowerShell:

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --no-daemon --max-workers=2
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`. It uses a development signing key and is for demonstration and testing, not store distribution.

Run the native walkthrough on a selected emulator, using its actual serial from `adb devices`:

```powershell
$env:ANDROID_SERIAL = 'emulator-5554'
.\gradlew.bat :app:connectedDebugAndroidTest --no-daemon --max-workers=2
```

The instrumentation suite uses synthetic credentials, clears this app's local demo session before each test, and captures real emulator screens. It does not operate on a physical phone. For screenshot collection, use direct instrumentation because the Gradle test runner may uninstall the app and delete its external files after a run. Assemble both APKs, install only on your selected emulator, then run and copy the captures:

```powershell
.\gradlew.bat :app:assembleDebug :app:assembleDebugAndroidTest
adb -s emulator-5554 install -r app/build/outputs/apk/debug/app-debug.apk
adb -s emulator-5554 install -r app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
adb -s emulator-5554 shell am instrument -w -r -e class com.example.simpleloginapp.LoginFlowTest com.example.simpleloginapp.test/androidx.test.runner.AndroidJUnitRunner
adb -s emulator-5554 pull /sdcard/Android/data/com.example.simpleloginapp/files/verification/. docs/verification/screenshots/native
```

## Browser companion

Node.js 20 or later is recommended. The deployed demo needs no server, database, environment variables, keys, or third-party scripts.

```powershell
npm ci
npm test
node scripts/serve-demo.mjs
```

Open `http://127.0.0.1:4173`. In another terminal, run:

```powershell
npx playwright install chromium
npx playwright test
```

If a compatible Chromium is already installed, set `PLAYWRIGHT_CHROMIUM_EXECUTABLE` to its executable path to reuse it. The test configuration accepts `DEMO_URL` to test the published site instead of starting a local server.

## Storage and security boundaries

Android stores only a demo login flag, the canonical sample email, and the session start time in private SharedPreferences. Logout clears them. Passwords are not persisted, restored in saved view state, or transmitted. Session metadata is excluded from cloud backup and device transfer. Older sessions that used arbitrary email addresses are discarded.

The web companion stores only the sample email and session time in `sessionStorage`. A refresh restores the current tab's session; closing the tab ends it. If browser storage is unavailable, the page falls back to an in-memory session and displays a notice. Logout clears saved metadata. JavaScript-disabled submissions are blocked and input fields have no form-data names. The deployed site also uses a restrictive Content Security Policy and disables camera, microphone, and geolocation permissions.

The sample password is deliberately public in source and UI. Local storage is editable by the device/browser owner. These mechanisms provide no identity verification, authorization, account security, or protection for sensitive data. A real service would require server-side authentication, appropriate credential handling, session enforcement, and a separate security review.

## Verification and changes

- [Results, environment, limitations, and publication evidence](docs/verification/RESULTS.md)
- [Numbered native screens and browser captures](docs/verification/SCREEN-INDEX.md)
- [Change rationale and commit sequence](docs/verification/CHANGES.md)

The Android app keeps its Kotlin, XML layouts, Material components, navigation graph, and purple visual theme. Changes focus on truthful demo behavior, lifecycle safety, input validation, readable English text, scrolling, and reproducible verification.
