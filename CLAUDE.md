# CLAUDE.md

This file provides guidance to Claude Code when working with this repository.

## Project Overview

Crossword Crazy is an Android crossword puzzle application built with Kotlin and Jetpack Compose. It follows MVVM architecture with Material Design 3 theming.

**Tech Stack:**
- Language: Kotlin 1.9.20
- UI: Jetpack Compose with Material Design 3
- Architecture: MVVM with StateFlow
- Min SDK: 24 (Android 7.0), Target SDK: 34

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug
```

## Test Commands

```bash
# Run unit tests
./gradlew test

# Run instrumented tests (requires emulator/device)
./gradlew connectedAndroidTest

# Run all checks
./gradlew check
```

## Project Structure

```
app/src/main/java/com/crossword/crazy/
├── MainActivity.kt           # Entry point
├── model/                    # Data models (Cell, Clue, CrosswordPuzzle, Direction)
├── data/                     # Data layer (PuzzleProvider)
├── viewmodel/                # State management (CrosswordViewModel)
└── ui/                       # Composable UI components
    ├── CrosswordScreen.kt    # Main screen
    ├── CrosswordGrid.kt      # Puzzle grid
    ├── CluesList.kt          # Clues display
    └── theme/                # Material Design 3 theme
```

## Architecture

- **Model**: Immutable data classes (`Cell`, `Clue`, `CrosswordPuzzle`, `Direction` enum)
- **Data**: `PuzzleProvider` singleton provides sample puzzles
- **ViewModel**: `CrosswordViewModel` manages `CrosswordUiState` via `StateFlow`
- **UI**: Composable functions collect state with `collectAsState()`

## Key Patterns

- **State Management**: Unidirectional data flow with `StateFlow`/`MutableStateFlow`
- **UI State**: Single `CrosswordUiState` data class contains all UI state
- **Cell Navigation**: ViewModel handles cursor movement with boundary checking
- **Keyboard Input**: Hidden `TextField` captures keyboard events

## Testing

- **Unit Tests** (`app/src/test/`): JUnit 4, MockK, Google Truth, Robolectric
- **Instrumented Tests** (`app/src/androidTest/`): Compose UI testing, Espresso
- Tests use `InstantTaskExecutorRule` for StateFlow/LiveData testing

## CI/CD

- **android-ci.yml**: Builds APKs and runs unit tests on push/PR
- **release.yml**: Creates GitHub release with APK on version tags (`v*`)

## Common Tasks

**Adding a new puzzle**: Edit `PuzzleProvider.kt` and add to the puzzles list

**Modifying UI state**: Update `CrosswordUiState` in `CrosswordViewModel.kt` and emit via `_uiState.value`

**Adding UI components**: Create composables in `ui/` and wire to `CrosswordScreen.kt`
