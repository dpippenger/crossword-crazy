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

        val puzzle = viewModel.uiState.value.puzzle!!
        puzzle.grid.flatten().forEach { cell ->
            if (!cell.isBlack) {
                cell.userInput = 'X'
            }
        }

        composeTestRule.onNodeWithText("Clear").performClick()
        composeTestRule.waitForIdle()

        val allCleared = puzzle.grid.flatten().all { it.userInput == null }
        assert(allCleared)
    }
}
