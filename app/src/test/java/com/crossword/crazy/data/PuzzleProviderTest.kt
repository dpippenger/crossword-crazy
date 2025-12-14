package com.crossword.crazy.data

import com.crossword.crazy.model.Direction
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PuzzleProviderTest {

    @Test
    fun `getSamplePuzzles returns non-empty list`() {
        val puzzles = PuzzleProvider.getSamplePuzzles()
        assertThat(puzzles).isNotEmpty()
    }

    @Test
    fun `getPuzzle returns valid puzzle`() {
        val puzzle = PuzzleProvider.getPuzzle(0)
        assertThat(puzzle).isNotNull()
        assertThat(puzzle.title).isNotEmpty()
        assertThat(puzzle.grid).isNotEmpty()
        assertThat(puzzle.clues).isNotEmpty()
    }

    @Test
    fun `puzzle grid has correct structure`() {
        val puzzle = PuzzleProvider.getPuzzle(0)
        val grid = puzzle.grid

        assertThat(grid).isNotEmpty()
        assertThat(grid.all { row -> row.size == puzzle.cols }).isTrue()
    }

    @Test
    fun `puzzle has both across and down clues`() {
        val puzzle = PuzzleProvider.getPuzzle(0)

        val hasAcrossClues = puzzle.clues.any { it.direction == Direction.ACROSS }
        val hasDownClues = puzzle.clues.any { it.direction == Direction.DOWN }

        assertThat(hasAcrossClues).isTrue()
        assertThat(hasDownClues).isTrue()
    }

    @Test
    fun `all clues have valid coordinates`() {
        val puzzle = PuzzleProvider.getPuzzle(0)

        puzzle.clues.forEach { clue ->
            assertThat(clue.startRow).isAtLeast(0)
            assertThat(clue.startRow).isLessThan(puzzle.rows)
            assertThat(clue.startCol).isAtLeast(0)
            assertThat(clue.startCol).isLessThan(puzzle.cols)
        }
    }

    @Test
    fun `all clue answers match grid`() {
        val puzzle = PuzzleProvider.getPuzzle(0)

        puzzle.clues.forEach { clue ->
            val cells = clue.getCells(puzzle.grid)
            val answer = cells.map { it.answer }.joinToString("")
            assertThat(answer).isEqualTo(clue.answer)
        }
    }
}
