# AGENTS.md

## Build & test

```sh
./gradlew assembleDebug                 # debug APK
./gradlew test                          # unit tests (host JVM)
./gradlew connectedDebugAndroidTest     # instrumented tests (emulator/device)
./gradlew lint                          # Android lint
```

Add all work-in-progress changes to a single commit in this repository.

Use `assembleDebug` not `assemble` — release builds are unconfigured (no signing key).

## Architecture

- **Single module** (`:app`), namespace `com.example.liks_sports`. No monorepo, no feature modules.
- **Zero DI** — `RoutinesViewModel` (the only ViewModel) is shared across all screens via `viewModel()`. `SettingsStore` and `ChatHistoryStore` are instantiated directly with `Context` inside composables using `remember { ... }`.
- **Persistence** — `SharedPreferences` + `org.json` (manual JSON). No Room, no kotlinx.serialization, no Gson/Moshi. `RoutinesViewModel`, `SettingsStore` (non-secret fields), and `ChatHistoryStore` write to `"liks_sports_prefs"`. The API key is stored in a separate encrypted prefs file using `androidx.security:security-crypto`.
- **State** — Compose `mutableStateOf` / `derivedStateOf` inside the ViewModel. No `StateFlow`, no `LiveData`.
- **Navigation** — Navigation Compose with three routes: routine list, routine detail, exercise timer. Route patterns and helpers live in `Routes` object (`app/src/main/java/com/example/liks_sports/ui/navigation/NavGraph.kt`).

## Dependencies

- **Version catalog** (`gradle/libs.versions.toml`) is the single source of truth. Never hardcode version numbers in build files. Use `alias(libs.plugins.xxx)` and `implementation(libs.xxx)`.
- **Kotlin 2.2.10** with the Compose compiler plugin (`org.jetbrains.kotlin.plugin.compose`). No separate `kotlin-android` plugin is applied — the Compose plugin handles both.
- **Zero external libraries** beyond standard Jetpack (compose-bom, activity-compose, material3, navigation-compose, lifecycle, security-crypto). Do not add Room, Retrofit, Hilt, Dagger, or any third-party HTTP/JSON library.
- **No Material Icons dependency** — all icons are custom vector drawables in `ui/icons/AppIcons.kt`. Do not add `material-icons-extended`.

## Key conventions

- **Kotlin code style**: `official` (per `gradle.properties`).
- **Built-in routines** use hardcoded IDs `"builtin_parkour"` and `"builtin_football"` — never add more builtins without updating `isBuiltin()` in `RoutinesViewModel`.
- **Edge-to-edge** is enabled in `MainActivity`. All screens should work with system bar insets.
- **Test directories**: unit tests in `app/src/test/`, instrumented tests in `app/src/androidTest/`.
- **ProGuard**: reverse-only rules, no minification for release builds. If minification is ever enabled, the `org.json` serialization in ViewModel needs keep rules.
- **Backup**: The prefs file with the encrypted API key is excluded from cloud backup via `backup_rules.xml`.
- **Permissions**: `INTERNET`, `ACCESS_NETWORK_STATE`, `VIBRATE`. The `ACCESS_NETWORK_STATE` supports connectivity checks before API calls.
