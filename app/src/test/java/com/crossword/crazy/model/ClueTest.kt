package com.crossword.crazy.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ClueTest {

    @Test
    fun `getCells returns correct cells for across clue`() {
        val grid = listOf(
            listOf(
                Cell(0, 0, 'C'),
                Cell(0, 1, 'A'),
                Cell(0, 2, 'T')
            )
        )

        val clue = Clue(
            number = 1,
            text = "Feline pet",
            direction = Direction.ACROSS,
            startRow = 0,
            startCol = 0,
            length = 3,
            answer = "CAT"
        )

        val cells = clue.getCells(grid)
        assertThat(cells).hasSize(3)
        assertThat(cells[0].answer).isEqualTo('C')
        assertThat(cells[1].answer).isEqualTo('A')
        assertThat(cells[2].answer).isEqualTo('T')
    }

    @Test
    fun `getCells returns correct cells for down clue`() {
        val grid = listOf(
            listOf(Cell(0, 0, 'C')),
            listOf(Cell(1, 0, 'A')),
            listOf(Cell(2, 0, 'R'))
        )

        val clue = Clue(
            number = 1,
            text = "Automobile",
            direction = Direction.DOWN,
            startRow = 0,
            startCol = 0,
            length = 3,
            answer = "CAR"
        )

        val cells = clue.getCells(grid)
        assertThat(cells).hasSize(3)
        assertThat(cells[0].answer).isEqualTo('C')
        assertThat(cells[1].answer).isEqualTo('A')
        assertThat(cells[2].answer).isEqualTo('R')
    }
}
