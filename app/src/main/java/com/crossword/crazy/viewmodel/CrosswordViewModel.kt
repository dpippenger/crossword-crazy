package com.crossword.crazy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crossword.crazy.data.PuzzleProvider
import com.crossword.crazy.model.Cell
import com.crossword.crazy.model.Clue
import com.crossword.crazy.model.CrosswordPuzzle
import com.crossword.crazy.model.Direction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CrosswordUiState(
    val puzzle: CrosswordPuzzle? = null,
    val selectedCell: Pair<Int, Int>? = null,
    val selectedDirection: Direction = Direction.ACROSS,
    val selectedClue: Clue? = null,
    val isPuzzleComplete: Boolean = false,
    val showCompletionDialog: Boolean = false,
    val checkResult: CheckResult = CheckResult.NONE
)

enum class CheckResult {
    NONE,
    CORRECT,
    INCORRECT
}

class CrosswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CrosswordUiState())
    val uiState: StateFlow<CrosswordUiState> = _uiState.asStateFlow()

    init {
        loadNewPuzzle(0)
    }

    fun loadNewPuzzle(index: Int = 0) {
        viewModelScope.launch {
            val puzzle = PuzzleProvider.getPuzzle(index)
            _uiState.value = CrosswordUiState(
                puzzle = puzzle,
                selectedCell = findFirstEmptyCell(puzzle),
                selectedDirection = Direction.ACROSS
            )
            updateSelectedClue()
        }
    }

    fun onCellSelected(row: Int, col: Int) {
        val currentState = _uiState.value
        val puzzle = currentState.puzzle ?: return
        val cell = puzzle.getCell(row, col) ?: return

        if (cell.isBlack) return

        val newDirection = if (currentState.selectedCell == Pair(row, col)) {
            if (currentState.selectedDirection == Direction.ACROSS) Direction.DOWN else Direction.ACROSS
        } else {
            currentState.selectedDirection
        }

        _uiState.value = currentState.copy(
            selectedCell = Pair(row, col),
            selectedDirection = newDirection,
            checkResult = CheckResult.NONE
        )
        updateSelectedClue()
    }

    fun onClueSelected(clue: Clue) {
        _uiState.value = _uiState.value.copy(
            selectedCell = Pair(clue.startRow, clue.startCol),
            selectedDirection = clue.direction,
            selectedClue = clue,
            checkResult = CheckResult.NONE
        )
    }

    fun onLetterInput(letter: Char) {
        val currentState = _uiState.value
        val puzzle = currentState.puzzle ?: return
        val (row, col) = currentState.selectedCell ?: return

        val cell = puzzle.getCell(row, col) ?: return
        if (cell.isBlack) return

        cell.userInput = letter.uppercaseChar()

        moveToNextCell()
        checkIfPuzzleComplete()
    }

    fun onBackspace() {
        val currentState = _uiState.value
        val puzzle = currentState.puzzle ?: return
        val (row, col) = currentState.selectedCell ?: return

        val cell = puzzle.getCell(row, col) ?: return
        if (cell.isBlack) return

        if (cell.userInput != null) {
            cell.userInput = null
        } else {
            moveToPreviousCell()
            val newState = _uiState.value
            val (newRow, newCol) = newState.selectedCell ?: return
            val newCell = puzzle.getCell(newRow, newCol)
            newCell?.userInput = null
        }

        _uiState.value = currentState.copy(
            isPuzzleComplete = false,
            showCompletionDialog = false
        )
    }

    fun checkAnswers() {
        val puzzle = _uiState.value.puzzle ?: return
        val isCorrect = puzzle.checkAnswers()

        _uiState.value = _uiState.value.copy(
            checkResult = if (isCorrect) CheckResult.CORRECT else CheckResult.INCORRECT
        )
    }

    fun revealCurrentAnswer() {
        val currentState = _uiState.value
        val clue = currentState.selectedClue ?: return
        val puzzle = currentState.puzzle ?: return

        puzzle.revealAnswer(clue)
        _uiState.value = currentState.copy(checkResult = CheckResult.NONE)
        checkIfPuzzleComplete()
    }

    fun clearAll() {
        val puzzle = _uiState.value.puzzle ?: return
        puzzle.clearAll()
        _uiState.value = _uiState.value.copy(
            isPuzzleComplete = false,
            showCompletionDialog = false,
            checkResult = CheckResult.NONE
        )
    }

    fun dismissCompletionDialog() {
        _uiState.value = _uiState.value.copy(showCompletionDialog = false)
    }

    private fun moveToNextCell() {
        val currentState = _uiState.value
        val puzzle = currentState.puzzle ?: return
        val (row, col) = currentState.selectedCell ?: return

        val (nextRow, nextCol) = when (currentState.selectedDirection) {
            Direction.ACROSS -> {
                var nextCol = col + 1
                var nextRow = row
                while (nextCol < puzzle.cols) {
                    val cell = puzzle.getCell(nextRow, nextCol)
                    if (cell != null && !cell.isBlack) {
                        break
                    }
                    nextCol++
                }
                if (nextCol >= puzzle.cols) return
                Pair(nextRow, nextCol)
            }
            Direction.DOWN -> {
                var nextRow = row + 1
                var nextCol = col
                while (nextRow < puzzle.rows) {
                    val cell = puzzle.getCell(nextRow, nextCol)
                    if (cell != null && !cell.isBlack) {
                        break
                    }
                    nextRow++
                }
                if (nextRow >= puzzle.rows) return
                Pair(nextRow, nextCol)
            }
        }

        _uiState.value = currentState.copy(selectedCell = Pair(nextRow, nextCol))
        updateSelectedClue()
    }

    private fun moveToPreviousCell() {
        val currentState = _uiState.value
        val puzzle = currentState.puzzle ?: return
        val (row, col) = currentState.selectedCell ?: return

        val (prevRow, prevCol) = when (currentState.selectedDirection) {
            Direction.ACROSS -> {
                var prevCol = col - 1
                var prevRow = row
                while (prevCol >= 0) {
                    val cell = puzzle.getCell(prevRow, prevCol)
                    if (cell != null && !cell.isBlack) {
                        break
                    }
                    prevCol--
                }
                if (prevCol < 0) return
                Pair(prevRow, prevCol)
            }
            Direction.DOWN -> {
                var prevRow = row - 1
                var prevCol = col
                while (prevRow >= 0) {
                    val cell = puzzle.getCell(prevRow, prevCol)
                    if (cell != null && !cell.isBlack) {
                        break
                    }
                    prevRow--
                }
                if (prevRow < 0) return
                Pair(prevRow, prevCol)
            }
        }

        _uiState.value = currentState.copy(selectedCell = Pair(prevRow, prevCol))
        updateSelectedClue()
    }

    private fun updateSelectedClue() {
        val currentState = _uiState.value
        val puzzle = currentState.puzzle ?: return
        val (row, col) = currentState.selectedCell ?: return

        val clue = puzzle.clues.find { clue ->
            clue.direction == currentState.selectedDirection &&
            when (clue.direction) {
                Direction.ACROSS -> row == clue.startRow && col >= clue.startCol && col < clue.startCol + clue.length
                Direction.DOWN -> col == clue.startCol && row >= clue.startRow && row < clue.startRow + clue.length
            }
        }

        _uiState.value = currentState.copy(selectedClue = clue)
    }

    private fun findFirstEmptyCell(puzzle: CrosswordPuzzle): Pair<Int, Int>? {
        for (row in 0 until puzzle.rows) {
            for (col in 0 until puzzle.cols) {
                val cell = puzzle.getCell(row, col)
                if (cell != null && !cell.isBlack) {
                    return Pair(row, col)
                }
            }
        }
        return null
    }

    private fun checkIfPuzzleComplete() {
        val puzzle = _uiState.value.puzzle ?: return
        if (puzzle.isComplete()) {
            val isCorrect = puzzle.checkAnswers()
            _uiState.value = _uiState.value.copy(
                isPuzzleComplete = true,
                showCompletionDialog = isCorrect
            )
        }
    }
}
