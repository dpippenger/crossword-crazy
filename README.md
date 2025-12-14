# Crossword Crazy

An Android crossword puzzle application built with Jetpack Compose and Kotlin.

## Features

- **Interactive Crossword Grid**: Touch-based interface for selecting and filling cells
- **Clue Navigation**: Browse clues organized by Across and Down
- **Game Controls**:
  - Check answers to validate your solution
  - Reveal answers for individual clues
  - Clear all entries to start fresh
- **Multiple Puzzles**: Sample puzzles included with varying difficulty
- **Completion Detection**: Automatic detection when puzzle is solved

## Architecture

This app follows modern Android development best practices:

- **UI**: Jetpack Compose with Material Design 3
- **Architecture**: MVVM (Model-View-ViewModel)
- **State Management**: StateFlow for reactive UI updates
- **Language**: Kotlin

### Project Structure

```
app/src/main/java/com/crossword/crazy/
├── model/              # Data models (Cell, Clue, CrosswordPuzzle)
├── data/               # Data providers and repositories
├── viewmodel/          # ViewModels for UI state management
├── ui/                 # Composable UI components
│   ├── theme/          # Theme and styling
│   ├── CrosswordGrid.kt
│   ├── CluesList.kt
│   └── CrosswordScreen.kt
└── MainActivity.kt
```

## Testing

The project includes comprehensive test coverage:

### Unit Tests (`app/src/test/`)
- Model tests for Cell, Clue, and CrosswordPuzzle
- PuzzleProvider tests for data integrity
- ViewModel tests for game logic

### Instrumented Tests (`app/src/androidTest/`)
- UI component tests for CrosswordGrid
- Interaction tests for CluesList
- Integration tests for CrosswordScreen

Run tests locally:
```bash
# Unit tests
./gradlew test

# Instrumented tests (requires emulator or device)
./gradlew connectedAndroidTest

# All tests
./gradlew check
```

## Continuous Integration

The project uses GitHub Actions for CI/CD:

- **Build**: Compiles the app and runs static analysis
- **Unit Tests**: Executes all unit tests
- **Instrumented Tests**: Runs UI tests on an Android emulator (optional)
- **APK Generation**: Builds release APK artifacts

See [`.github/workflows/android-ci.yml`](.github/workflows/android-ci.yml) for details.

## Releases

The project automatically creates GitHub releases with APK artifacts when you push a version tag.

### Creating a Release

To create a new release:

```bash
# Create and push a version tag
git tag v1.0.0
git push origin v1.0.0
```

The GitHub Actions workflow will automatically:
1. Build the release APK
2. Create a GitHub release with release notes
3. Attach the APK to the release

The APK will be named `crossword-crazy-v1.0.0.apk` and can be downloaded from the [Releases](../../releases) page.

### Version Numbering

We use semantic versioning (MAJOR.MINOR.PATCH):
- **MAJOR**: Incompatible API changes
- **MINOR**: New functionality in a backwards compatible manner
- **PATCH**: Backwards compatible bug fixes

Tags should follow the format: `v1.0.0`, `v1.1.0`, `v2.0.0`, etc.

## Building

### Requirements
- JDK 17 or higher
- Android SDK with API 34
- Gradle 8.2 (included via wrapper)

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug

# Run the app
adb shell am start -n com.crossword.crazy/.MainActivity
```

## How to Play

1. Launch the app to see the first puzzle
2. Tap any cell in the grid to select it
3. The current clue will be highlighted below the grid
4. Type letters using your keyboard to fill in answers
5. Tap a cell again to switch between Across and Down directions
6. Use the tabs below to browse all clues
7. Tap on any clue to jump to that position
8. Use the action buttons:
   - **Check**: Validate all your answers
   - **Reveal**: Show the answer for the current clue
   - **Clear**: Remove all your entries
   - **New Game**: Load a different puzzle

## License

See [LICENSE](LICENSE) file for details.

## Development

This project was created as a demonstration of:
- Modern Android development with Jetpack Compose
- MVVM architecture pattern
- Comprehensive testing strategies
- CI/CD with GitHub Actions

Pull requests and improvements are welcome!
