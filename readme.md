# Box/MMA Timer

A simple Android interval timer for boxing, martial arts and similar training. Configure the round length, rest length and number of rounds, save timers on the device, and run them.

The app is written in Java and uses no Google services (no Play Services, Firebase or analytics). It is designed to work on GrapheneOS and on other Android devices.

> **Status:** work in progress.

## Features

- Configurable timer: round time, rest time and number of rounds (minutes and seconds)
- Timers saved locally on the device
- Start, pause, resume and reset
- Round and rest countdowns with a rounds-left counter
- Sound alert when a round or rest ends

## Tech stack

| Area | Choice |
|---|---|
| Language | Java |
| UI | XML layouts, Material Components |
| Architecture | MVVM (Activity, ViewModel, Repository, DAO) |
| Storage | Room (local SQLite) |
| Logging | Timber |
| Build | Gradle (Kotlin DSL, version catalog) |

## Requirements

- JDK 17 or newer
- Android SDK with a recent platform and build tools (install with `sdkmanager`)
- `minSdk` 26 (Android 8.0)
- IntelliJ IDEA with the Android plugin, or Android Studio

## Getting started

1. Clone the repository.
2. Point Gradle at your SDK by creating `local.properties` in the project root (this file is not committed):

   ```
   sdk.dir=/home/YOUR_USER/Android/Sdk
   ```

3. Build and install on a connected device or emulator:

   ```bash
   ./gradlew installDebug
   ```

## Testing

> **Status:** To be done

```bash
./gradlew test                    # local unit tests (domain logic)
./gradlew connectedAndroidTest    # instrumented tests (Room, needs a device or emulator)
```

Domain logic (timer engine, time handling) lives in plain Java so it can be tested on the JVM with a fake clock. Anything that needs a real Android environment, such as Room, goes in `androidTest`.

For an emulator without Google services, use an AOSP ("Android Open Source Project") system image.

## Project structure

```
com.german.boxtimer
├── converter/    Converters for the dao
├── activity/     Screens (Activities)
├── viewModel/    UI state exposed as LiveData
├── domain/       Timer rules, plain Java
├── repository/   Single entry point to data, handles threading
├── dao/          Room queries
├── entity/       Room entities
└── utils/        Time formatting helpers, constants and ViewModelFactory
```

How the layers connect:

```
Activity -> ViewModel -> Repository -> DAO -> Room database
                 |
                 +-> domain (timer engine)
```

## Design notes

- **Time is computed, not counted.** The timer stores an end timestamp and calculates the remaining time on each tick, using `SystemClock.elapsedRealtime()`, so late ticks do not cause drift.
- **No database work on the main thread.** All repository calls run on an executor, and results come back through callbacks or LiveData.
- **No Google dependencies.** Check with:

  ```bash
  ./gradlew app:dependencies | grep -iE "play-services|firebase|gms"
  ```

  No output means the project is clean.

## Roadmap to min functionality

- [x] Basic minutes and seconds configurable timer
- [x] Timer start, pause and reset work 
- [x] Bells sounds
- [ ] UI
  - [x] Basic UI for testing
  - [ ] Proper usable UI
- [ ] Run timers in a foreground service so they keep going with the screen off
- [ ] Exact alarms and notifications
- [ ] Stopwatch mode with laps
- [ ] Settings (sound, vibration, theme)
