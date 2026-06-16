# AGENTS.md — liks-sports

Single-module Android app (Jetpack Compose, no XML).  
Entrypoint: `app/src/main/java/com/example/liks_sports/MainActivity.kt`

## Commands

```sh
./gradlew assembleDebug               # build
./gradlew test                         # local JUnit 4 unit tests
./gradlew connectedDebugAndroidTest    # Compose UI tests (emulator/device)
./gradlew lint                         # Android lint
```

No CI workflows exist (no `.github/`). No ktfmt/ktlint/spotless config.

## Architecture

- **3 screens:** `RoutineListScreen`, `RoutineDetailScreen`, `ExerciseTimerScreen` — navigation via `NavGraph.kt` (`Routes` object).
- **Single ViewModel** (`RoutinesViewModel`) created at the `AppNavHost` level with `viewModel()`, passed as callbacks to screens — screens never hold a ViewModel reference.
- **ViewModel state** uses `mutableStateOf` (Compose observable), not `StateFlow`. Read/write synchronously.
- **Persistence** via `SharedPreferences` + `org.json` (zero external deps). `RoutinesViewModel` extends `AndroidViewModel` — saves/loads routines on every mutation and on init. Change fields in `Exercise.kt` and `Routine` must be mirrored in `RoutinesViewModel.fromJson()`/`toJson()`.
- **`data/` package** has only two files: `Exercise.kt` (data classes) and `RoutinesViewModel.kt`. No repository/DAO layer.
- **Custom vector icons** in `ui/icons/AppIcons.kt`, not Material Icons.
- `enableEdgeToEdge()` called in `MainActivity.onCreate`.
- `RoutineDetailScreen` manages a local copy of exercises before committing via `onUpdateRoutine`.
- `ExerciseTimerScreen` uses `rememberSaveable` extensively (survives config changes), `DisposableEffect` for screen-on + auto-start, and plays a notification sound + vibration on each transition.

## Config

- Version catalog: `gradle/libs.versions.toml`
- `minSdk 24 / targetSdk 36 / compileSdk 36`, Kotlin 2.2.10, AGP 9.2.1, Compose BOM 2026.02.01
- `kotlin.code.style=official` in `gradle.properties`

## Gotchas

- `connectedDebugAndroidTest` requires emulator/device; `test` is local-only.
- `Exercise` and `Routine` data classes generate UUIDs on construction — not stable for test assertions expecting fixed IDs.
- `RoutinesViewModel.addRoutine()` returns the created `Routine` — capture it for assertions.
- `Routes.timer()` defaults `repeatCount` to `1`, matching the nav argument `defaultValue = 1` in `NavGraph.kt`.
- Existing test files (`test/` and `androidTest/`) are boilerplate stubs — real tests need to be written.
