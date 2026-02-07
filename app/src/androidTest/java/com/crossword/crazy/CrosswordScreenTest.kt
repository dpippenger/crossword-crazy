package com.crossword.crazy

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.crossword.crazy.ui.CrosswordScreen
import com.crossword.crazy.ui.theme.CrosswordCrazyTheme
import com.crossword.crazy.viewmodel.CrosswordViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CrosswordScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun crosswordScreen_displaysTitle() {
        val viewModel = CrosswordViewModel()

        composeTestRule.setContent {
            CrosswordCrazyTheme {
                CrosswordScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Simple Puzzle", substring = true).assertExists()
    }

    @Test
    fun crosswordScreen_displaysNewGameButton() {
        val viewModel = CrosswordViewModel()

        composeTestRule.setContent {
            CrosswordCrazyTheme {
                CrosswordScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("New Game").assertExists()
    }

    @Test
    fun crosswordScreen_displaysActionButtons() {
        val viewModel = CrosswordViewModel()

        composeTestRule.setContent {
            CrosswordCrazyTheme {
                CrosswordScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Check").assertExists()
        composeTestRule.onNodeWithText("Reveal").assertExists()
        composeTestRule.onNodeWithText("Clear").assertExists()
    }

    @Test
    fun crosswordScreen_displaysCluesTabs() {
        val viewModel = CrosswordViewModel()

        composeTestRule.setContent {
            CrosswordCrazyTheme {
                CrosswordScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Across").assertExists()
        composeTestRule.onNodeWithText("Down").assertExists()
    }

    @Test
    fun crosswordScreen_newGameButton_loadsNewPuzzle() {
        val viewModel = CrosswordViewModel()

        composeTestRule.setContent {
            CrosswordCrazyTheme {
                CrosswordScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("New Game").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Simple Puzzle", substring = true).assertExists()
    }

    @Test
    fun crosswordScreen_clearButton_clearsInput() {
        val viewModel = CrosswordViewModel()

        composeTestRule.setContent {
            CrosswordCrazyTheme {
                CrosswordScreen(viewModel = viewModel)
            }
        }

        // Fill cells via ViewModel API instead of direct mutation
        val puzzle = viewModel.uiState.value.puzzle!!
        puzzle.grid.flatten().forEach { cell ->
            if (!cell.isBlack) {
                viewModel.onCellSelected(cell.row, cell.col)
                viewModel.onLetterInput('X')
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Clear").performClick()
        composeTestRule.waitForIdle()

        val clearedPuzzle = viewModel.uiState.value.puzzle!!
        val allCleared = clearedPuzzle.grid.flatten().all { it.userInput == null }
        assert(allCleared)
    }
}
