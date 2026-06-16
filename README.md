# liks-sports

Workout routine timer for Android — built with Jetpack Compose.

Create custom routines with exercises, set duration/rest per exercise, run a hands-free timer with audio & vibration cues, and edit routines with an AI assistant (any OpenAI-compatible API).

## Features

- **Routine management** — create, rename, delete custom workout routines
- **Built-in routines** — Parkour and Football (Fútbol) included out of the box
- **Per-exercise configuration** — reps, exercise duration, rest duration, override global defaults
- **Global defaults** — set exercise/rest time for all non-overridden exercises at once
- **Workout timer** — hands-free timer with circular + linear progress indicators, play/pause/skip/reset, notification sound & vibration on each transition, keeps screen on
- **Repeat count** — repeat the full routine up to 10 times
- **AI assistant** — chat with an LLM to add, remove, rename, or modify exercises (requires an API key)
- **Dynamic color** — Material 3 theming with Android 12+ dynamic color support, dark/light mode
- **Edge-to-edge** — full-bleed display

## Screens

| Screen | Description |
|--------|-------------|
| Routine List | All routines (built-in + custom). Tap to edit, long-press-like actions for rename/delete. |
| Routine Detail | Edit exercises, set global defaults, repeat count, and start workout. AI edit button opens the chat dialog. |
| Exercise Timer | Full-screen timer showing current exercise/rest phase, countdown, rep tracking, and overall progress. |

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 2.2.10 |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| Architecture | Single-ViewModel (AndroidViewModel), Compose `mutableStateOf` |
| Persistence | SharedPreferences + `org.json` |
| Min / Target SDK | 24 / 36 |
| Compose BOM | 2026.02.01 |
| AGP | 9.2.1 |

Zero external dependencies beyond the standard Jetpack suite — no Room, Retrofit, Hilt, or Dagger.

## Project Structure

```
app/src/main/java/com/example/liks_sports/
├── MainActivity.kt              # Entry point, edge-to-edge setup
├── data/
│   ├── Exercise.kt              # Exercise & Routine data classes
│   ├── RoutinesViewModel.kt     # ViewModel, persistence, built-in routines
│   ├── SettingsStore.kt         # AI API config (url, key, model)
│   └── ChatHistoryStore.kt      # Per-routine chat history
└── ui/
    ├── icons/AppIcons.kt        # Custom vector icons (no Material Icons dep)
    ├── navigation/NavGraph.kt   # Routes + NavHost setup
    ├── screens/
    │   ├── RoutineListScreen.kt
    │   ├── RoutineDetailScreen.kt
    │   ├── ExerciseTimerScreen.kt
    │   ├── SettingsDialog.kt
    │   └── AiChatDialog.kt
    └── theme/
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

## Building

```sh
./gradlew assembleDebug          # Debug APK
./gradlew test                   # Local unit tests
./gradlew connectedDebugAndroidTest  # Instrumented UI tests (emulator/device)
./gradlew lint                   # Android lint
```

## AI Assistant Setup

1. Tap the settings gear icon
2. Enter your API base URL (e.g. `https://api.openai.com/v1`), API key, and model ID
3. Open any routine and tap the sparkle icon to open the AI chat

The AI uses function-calling-like JSON actions to edit the routine directly. Supports streaming and reasoning content.

## License

MIT
