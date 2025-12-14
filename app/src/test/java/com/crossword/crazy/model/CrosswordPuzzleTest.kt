package com.crossword.crazy.model

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class CrosswordPuzzleTest {

    private lateinit var puzzle: CrosswordPuzzle

    @Before
    fun setup() {
        val grid = listOf(
            listOf(
                Cell(0, 0, 'C', number = 1),
                Cell(0, 1, 'A'),
                Cell(0, 2, 'T')
            ),
            listOf(
                Cell(1, 0, 'A'),
                Cell(1, 1, null),
                Cell(1, 2, 'O')
            ),
            listOf(
                Cell(2, 0, 'R', number = 4),
                Cell(2, 1, 'E'),
                Cell(2, 2, 'D')
            )
        )

        val clues = listOf(
            Clue(1, "Feline pet", Direction.ACROSS, 0, 0, 3, "CAT"),
            Clue(4, "Primary color", Direction.ACROSS, 2, 0, 3, "RED"),
            Clue(1, "Automobile", Direction.DOWN, 0, 0, 3, "CAR")
        )

        puzzle = CrosswordPuzzle("Test Puzzle", grid, clues)
    }

    @Test
    fun `puzzle has correct dimensions`() {
        assertThat(puzzle.rows).isEqualTo(3)
        assertThat(puzzle.cols).isEqualTo(3)
    }

    @Test
    fun `acrossClues returns only across clues`() {
        val acrossClues = puzzle.acrossClues
        assertThat(acrossClues).hasSize(2)
        assertThat(acrossClues.all { it.direction == Direction.ACROSS }).isTrue()
    }

    @Test
    fun `downClues returns only down clues`() {
        val downClues = puzzle.downClues
        assertThat(downClues).hasSize(1)
        assertThat(downClues.all { it.direction == Direction.DOWN }).isTrue()
    }

    @Test
    fun `getCell returns correct cell`() {
        val cell = puzzle.getCell(0, 0)
        assertThat(cell?.answer).isEqualTo('C')
        assertThat(cell?.number).isEqualTo(1)
    }

    @Test
    fun `getCell returns null for invalid coordinates`() {
        assertThat(puzzle.getCell(-1, 0)).isNull()
        assertThat(puzzle.getCell(0, -1)).isNull()
        assertThat(puzzle.getCell(10, 0)).isNull()
        assertThat(puzzle.getCell(0, 10)).isNull()
    }

    @Test
    fun `isComplete returns false when puzzle has empty cells`() {
        assertThat(puzzle.isComplete()).isFalse()
    }

    @Test
    fun `isComplete returns true when all cells filled`() {
        puzzle.grid.flatten().forEach { cell ->
            if (!cell.isBlack) {
                cell.userInput = cell.answer
            }
        }
        assertThat(puzzle.isComplete()).isTrue()
    }

    @Test
    fun `checkAnswers returns true when all answers correct`() {
        puzzle.grid.flatten().forEach { cell ->
            if (!cell.isBlack) {
                cell.userInput = cell.answer
            }
        }
        assertThat(puzzle.checkAnswers()).isTrue()
    }

    @Test
    fun `checkAnswers returns false when answers incorrect`() {
        puzzle.grid.flatten().forEach { cell ->
            if (!cell.isBlack) {
                cell.userInput = 'X'
            }
        }
        assertThat(puzzle.checkAnswers()).isFalse()
    }

    @Test
    fun `clearAll removes all user input`() {
        puzzle.grid.flatten().forEach { cell ->
            if (!cell.isBlack) {
                cell.userInput = 'X'
            }
        }
        puzzle.clearAll()
        assertThat(puzzle.grid.flatten().all { it.userInput == null }).isTrue()
    }

    @Test
    fun `revealAnswer fills in correct answer for clue`() {
        val clue = puzzle.acrossClues[0]
        puzzle.revealAnswer(clue)

        val cells = clue.getCells(puzzle.grid)
        assertThat(cells[0].userInput).isEqualTo('C')
        assertThat(cells[1].userInput).isEqualTo('A')
        assertThat(cells[2].userInput).isEqualTo('T')
    }
}
