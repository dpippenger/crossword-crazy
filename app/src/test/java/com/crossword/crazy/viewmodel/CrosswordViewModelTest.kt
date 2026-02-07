package com.crossword.crazy.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.crossword.crazy.model.Direction
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CrosswordViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CrosswordViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CrosswordViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has puzzle loaded`() {
        val state = viewModel.uiState.value
        assertThat(state.puzzle).isNotNull()
    }

    @Test
    fun `initial state has selected cell`() {
        val state = viewModel.uiState.value
        assertThat(state.selectedCell).isNotNull()
    }

    @Test
    fun `loadNewPuzzle loads a puzzle`() {
        viewModel.loadNewPuzzle(0)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.puzzle).isNotNull()
        assertThat(state.isPuzzleComplete).isFalse()
    }

    @Test
    fun `onCellSelected updates selected cell`() {
        val state = viewModel.uiState.value
        val puzzle = state.puzzle!!

        val cell = puzzle.grid[0][0]
        if (!cell.isBlack) {
            viewModel.onCellSelected(0, 0)
            testDispatcher.scheduler.advanceUntilIdle()

            val newState = viewModel.uiState.value
            assertThat(newState.selectedCell).isEqualTo(Pair(0, 0))
        }
    }

    @Test
    fun `onCellSelected toggles direction when selecting same cell`() {
        val puzzle = viewModel.uiState.value.puzzle!!
        val cell = puzzle.grid[0][0]

        if (!cell.isBlack) {
            viewModel.onCellSelected(0, 0)
            testDispatcher.scheduler.advanceUntilIdle()
            val direction1 = viewModel.uiState.value.selectedDirection

            viewModel.onCellSelected(0, 0)
            testDispatcher.scheduler.advanceUntilIdle()
            val direction2 = viewModel.uiState.value.selectedDirection

            assertThat(direction1).isNotEqualTo(direction2)
        }
    }

    @Test
    fun `onLetterInput updates cell`() {
        val state = viewModel.uiState.value
        val (row, col) = state.selectedCell!!

        viewModel.onLetterInput('A')
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedPuzzle = viewModel.uiState.value.puzzle!!
        val cell = updatedPuzzle.getCell(row, col)
        assertThat(cell?.userInput).isEqualTo('A')
    }

    @Test
    fun `onBackspace clears cell input`() {
        val state = viewModel.uiState.value
        val (row, col) = state.selectedCell!!

        viewModel.onLetterInput('A')
        testDispatcher.scheduler.advanceUntilIdle()

        // After input, cursor moves to next cell. Select back to original cell.
        viewModel.onCellSelected(row, col)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onBackspace()
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedPuzzle = viewModel.uiState.value.puzzle!!
        val cell = updatedPuzzle.getCell(row, col)
        assertThat(cell?.userInput).isNull()
    }

    @Test
    fun `checkAnswers returns correct when all answers right`() {
        val puzzle = viewModel.uiState.value.puzzle!!

        // Fill all cells with correct answers via ViewModel
        puzzle.grid.flatten().forEach { cell ->
            if (!cell.isBlack) {
                viewModel.onCellSelected(cell.row, cell.col)
                testDispatcher.scheduler.advanceUntilIdle()
                viewModel.onLetterInput(cell.answer!!)
                testDispatcher.scheduler.advanceUntilIdle()
            }
        }

        viewModel.checkAnswers()
        testDispatcher.scheduler.advanceUntilIdle()

        val newState = viewModel.uiState.value
        assertThat(newState.checkResult).isEqualTo(CheckResult.CORRECT)
    }

    @Test
    fun `checkAnswers returns incorrect when answers wrong`() {
        val puzzle = viewModel.uiState.value.puzzle!!

        // Fill all cells with wrong answers via ViewModel
        puzzle.grid.flatten().forEach { cell ->
            if (!cell.isBlack) {
                viewModel.onCellSelected(cell.row, cell.col)
                testDispatcher.scheduler.advanceUntilIdle()
                viewModel.onLetterInput('X')
                testDispatcher.scheduler.advanceUntilIdle()
            }
        }

        viewModel.checkAnswers()
        testDispatcher.scheduler.advanceUntilIdle()

        val newState = viewModel.uiState.value
        assertThat(newState.checkResult).isEqualTo(CheckResult.INCORRECT)
    }

    @Test
    fun `clearAll removes all user input`() {
        val puzzle = viewModel.uiState.value.puzzle!!

        // Fill cells via ViewModel
        puzzle.grid.flatten().forEach { cell ->
            if (!cell.isBlack) {
                viewModel.onCellSelected(cell.row, cell.col)
                testDispatcher.scheduler.advanceUntilIdle()
                viewModel.onLetterInput('X')
                testDispatcher.scheduler.advanceUntilIdle()
            }
        }

        viewModel.clearAll()
        testDispatcher.scheduler.advanceUntilIdle()

        val clearedPuzzle = viewModel.uiState.value.puzzle!!
        assertThat(clearedPuzzle.grid.flatten().all { it.userInput == null }).isTrue()
    }

    @Test
    fun `revealCurrentAnswer fills in answer for selected clue`() {
        viewModel.onClueSelected(viewModel.uiState.value.puzzle!!.clues[0])
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.revealCurrentAnswer()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        val clue = state.selectedClue!!
        val cells = clue.getCells(state.puzzle!!.grid)

        assertThat(cells.all { it.isCorrect }).isTrue()
    }

    @Test
    fun `onClueSelected updates selected clue and cell`() {
        val clue = viewModel.uiState.value.puzzle!!.clues[0]

        viewModel.onClueSelected(clue)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.selectedClue).isEqualTo(clue)
        assertThat(state.selectedCell).isEqualTo(Pair(clue.startRow, clue.startCol))
        assertThat(state.selectedDirection).isEqualTo(clue.direction)
    }
}
