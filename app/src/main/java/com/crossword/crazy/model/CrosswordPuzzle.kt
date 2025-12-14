package com.crossword.crazy.model

data class CrosswordPuzzle(
    val title: String,
    val grid: List<List<Cell>>,
    val clues: List<Clue>
) {
    val rows: Int = grid.size
    val cols: Int = if (grid.isNotEmpty()) grid[0].size else 0

    val acrossClues: List<Clue>
        get() = clues.filter { it.direction == Direction.ACROSS }.sortedBy { it.number }

    val downClues: List<Clue>
        get() = clues.filter { it.direction == Direction.DOWN }.sortedBy { it.number }

    fun getCell(row: Int, col: Int): Cell? {
        return if (row in 0 until rows && col in 0 until cols) {
            grid[row][col]
        } else {
            null
        }
    }

    fun isComplete(): Boolean {
        return grid.flatten().all { cell ->
            cell.isBlack || cell.userInput != null
        }
    }

    fun checkAnswers(): Boolean {
        return grid.flatten().all { cell ->
            cell.isBlack || cell.isCorrect
        }
    }

    fun clearAll() {
        grid.flatten().forEach { it.clear() }
    }

    fun revealAnswer(clue: Clue) {
        val cells = clue.getCells(grid)
        cells.forEach { cell ->
            cell.userInput = cell.answer
        }
    }
}
