package com.crossword.crazy.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CellTest {

    @Test
    fun `cell with null answer is black`() {
        val cell = Cell(row = 0, col = 0, answer = null)
        assertThat(cell.isBlack).isTrue()
    }

    @Test
    fun `cell with answer is not black`() {
        val cell = Cell(row = 0, col = 0, answer = 'A')
        assertThat(cell.isBlack).isFalse()
    }

    @Test
    fun `cell without user input is empty`() {
        val cell = Cell(row = 0, col = 0, answer = 'A')
        assertThat(cell.isEmpty).isTrue()
    }

    @Test
    fun `cell with user input is not empty`() {
        val cell = Cell(row = 0, col = 0, answer = 'A', userInput = 'A')
        assertThat(cell.isEmpty).isFalse()
    }

    @Test
    fun `cell with correct user input is correct`() {
        val cell = Cell(row = 0, col = 0, answer = 'A', userInput = 'A')
        assertThat(cell.isCorrect).isTrue()
    }

    @Test
    fun `cell with incorrect user input is not correct`() {
        val cell = Cell(row = 0, col = 0, answer = 'A', userInput = 'B')
        assertThat(cell.isCorrect).isFalse()
    }

    @Test
    fun `clear removes user input`() {
        val cell = Cell(row = 0, col = 0, answer = 'A', userInput = 'B')
        cell.clear()
        assertThat(cell.userInput).isNull()
    }
}
