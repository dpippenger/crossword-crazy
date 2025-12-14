package com.crossword.crazy

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.crossword.crazy.data.PuzzleProvider
import com.crossword.crazy.model.Clue
import com.crossword.crazy.ui.CluesList
import com.crossword.crazy.ui.theme.CrosswordCrazyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CluesListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cluesList_displaysAcrossCluess() {
        val puzzle = PuzzleProvider.getPuzzle(0)
        var selectedClue: Clue? = null

        composeTestRule.setContent {
            CrosswordCrazyTheme {
                CluesList(
                    acrossClues = puzzle.acrossClues,
                    downClues = puzzle.downClues,
                    selectedClue = null,
                    onClueClick = { selectedClue = it }
                )
            }
        }

        composeTestRule.onNodeWithText("Across").assertExists()
        composeTestRule.onNodeWithText("Across").performClick()
        composeTestRule.waitForIdle()

        puzzle.acrossClues.firstOrNull()?.let { clue ->
            composeTestRule.onNodeWithText(clue.text, substring = true).assertExists()
        }
    }

    @Test
    fun cluesList_displaysDownClues() {
        val puzzle = PuzzleProvider.getPuzzle(0)
        var selectedClue: Clue? = null

        composeTestRule.setContent {
            CrosswordCrazyTheme {
                CluesList(
                    acrossClues = puzzle.acrossClues,
                    downClues = puzzle.downClues,
                    selectedClue = null,
                    onClueClick = { selectedClue = it }
                )
            }
        }

        composeTestRule.onNodeWithText("Down").assertExists()
        composeTestRule.onNodeWithText("Down").performClick()
        composeTestRule.waitForIdle()

        puzzle.downClues.firstOrNull()?.let { clue ->
            composeTestRule.onNodeWithText(clue.text, substring = true).assertExists()
        }
    }

    @Test
    fun cluesList_clueClick_triggersCallback() {
        val puzzle = PuzzleProvider.getPuzzle(0)
        var selectedClue: Clue? = null

        composeTestRule.setContent {
            CrosswordCrazyTheme {
                CluesList(
                    acrossClues = puzzle.acrossClues,
                    downClues = puzzle.downClues,
                    selectedClue = null,
                    onClueClick = { selectedClue = it }
                )
            }
        }

        puzzle.acrossClues.firstOrNull()?.let { clue ->
            composeTestRule.onNodeWithText(clue.text, substring = true).performClick()
            composeTestRule.waitForIdle()
            assert(selectedClue != null)
        }
    }

    @Test
    fun cluesList_switchingTabs_showsDifferentClues() {
        val puzzle = PuzzleProvider.getPuzzle(0)

        composeTestRule.setContent {
            CrosswordCrazyTheme {
                CluesList(
                    acrossClues = puzzle.acrossClues,
                    downClues = puzzle.downClues,
                    selectedClue = null,
                    onClueClick = { }
                )
            }
        }

        composeTestRule.onNodeWithText("Across").performClick()
        composeTestRule.waitForIdle()

        val firstAcrossClue = puzzle.acrossClues.first()
        composeTestRule.onNodeWithText(firstAcrossClue.text, substring = true).assertExists()

        composeTestRule.onNodeWithText("Down").performClick()
        composeTestRule.waitForIdle()

        val firstDownClue = puzzle.downClues.first()
        composeTestRule.onNodeWithText(firstDownClue.text, substring = true).assertExists()
    }
}
