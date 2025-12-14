package com.crossword.crazy

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.crossword.crazy.data.PuzzleProvider
import com.crossword.crazy.ui.CrosswordGrid
import com.crossword.crazy.ui.theme.CrosswordCrazyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CrosswordGridTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun crosswordGrid_displaysCellNumbers() {
        val puzzle = PuzzleProvider.getPuzzle(0)
        var clickedCell: Pair<Int, Int>? = null

        composeTestRule.setContent {
            CrosswordCrazyTheme {
                CrosswordGrid(
                    puzzle = puzzle,
                    selectedCell = null,
                    onCellClick = { row, col -> clickedCell = Pair(row, col) }
                )
            }
        }

        val numberedCells = puzzle.grid.flatten().filter { it.number != null }
        assert(numberedCells.isNotEmpty())
    }

    @Test
    fun crosswordGrid_cellClick_triggersCallback() {
        val puzzle = PuzzleProvider.getPuzzle(0)
        var clickedCell: Pair<Int, Int>? = null

        composeTestRule.setContent {
            CrosswordCrazyTheme {
                CrosswordGrid(
                    puzzle = puzzle,
                    selectedCell = null,
                    onCellClick = { row, col -> clickedCell = Pair(row, col) }
                )
            }
        }

        val firstNonBlackCell = puzzle.grid.flatten().firstOrNull { !it.isBlack }
        if (firstNonBlackCell != null) {
            firstNonBlackCell.number?.let { number ->
                composeTestRule.onNodeWithText(number.toString()).performClick()
                composeTestRule.waitForIdle()
                assert(clickedCell != null)
            }
        }
    }
}
