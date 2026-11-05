# Changes and commit sequence

## Findings and decisions

The actual app is an offline Kotlin/Material learning demo for a login/profile/logout flow. It has no backend, registration, business records, or identity provider. Improvements preserve that scope and the existing Android stack.

1. Removed the nested duplicate project after comparing its source and resources with the root app. Keeping two builds with different plugin versions was a maintenance and setup risk.
2. Removed the committed machine-specific JDK path and corrected API 26-only button padding while preserving minimum SDK 24.
3. Replaced permissive fake authentication with an explicit public demo-credential policy. The displayed invalid-credential state is now reachable, and password whitespace is no longer silently altered.
4. Stored canonical sample session metadata only, discarded incompatible old sessions, excluded backup/transfer, and stopped password view-state persistence.
5. Removed delayed fragment callbacks and managed logout-dialog lifetime with the view. Navigation uses current-destination guards and clears the previous destination through the existing graph.
6. Made login and profile scrollable in portrait, landscape, and keyboard-constrained layouts. Updated labels and errors to plain English, added IME Done submission and accessible headings, and corrected contrast observed in native screenshots under a light system theme.
7. Replaced unsupported premium/member-since claims with truthful local-demo and session-start information. No registration or production-authentication feature was invented.
8. Added a static browser companion with explicit storage/security boundaries, public sample values, blocked-storage handling, keyboard operation, and no-JavaScript submission protection.
9. Added meaningful Kotlin, native, and browser regression tests, real screenshots, a signed debug APK, and anonymous checks of the public deployment and artifact integrity.

## Git commits

All new commits use the repository's established identity, `luigidev <cluiz4003@outlook.pt>`, verified against the connected owner's email. Existing commit history and attribution are preserved.

| Commit | Purpose |
| --- | --- |
| `e748cc6692bb4d1b5c69fd0b04ee7e9d754667f8` | Consolidate the Android project, portable Java configuration, API-compatible padding, generated-file rules |
| `b20c8319153820b23b25e9fbb9e2f6ec050146e1` | Responsive interactive browser companion, storage policy, browser/unit tests, static build tooling |
| `4c62fd036984b4e3317726b48a8e775a3e05bdbb` | Strict demo credentials, lifecycle-safe sessions/navigation, English scrollable native UI, contrast fixes, native/JVM tests |
| `c2c1936456e0b6cc64131f69c947a6f760925a69` | Signed debug APK, root static-hosting configuration, browser error evidence capture |

Subsequent documentation/evidence commits are visible in the branch log. Every implementation commit includes the concrete problem, resulting behavior, validation and practical limitations. The review branch is `improve/login-demo`; `master` was not updated. The original tool-prefixed working branch was renamed locally before publication. Historical authorship and required attribution were not rewritten.

## Screenshots and test records

[SCREEN-INDEX.md](SCREEN-INDEX.md) maps each reachable Android destination, dialog, validation state, rotation view, and browser state to its capture. Browser screenshots are labeled separately from native emulator pixels. Diagnostic ANR captures are excluded from successful screen evidence.

[RESULTS.md](RESULTS.md) records toolchain versions, test results, public URLs, APK digest, emulator issues, and limits. The demo has no server/account setup or hardware integration requirement. Nothing here establishes production authentication or verified user identity.
