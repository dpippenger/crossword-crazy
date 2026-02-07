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

    fun withCellInput(row: Int, col: Int, input: Char?): CrosswordPuzzle {
        val cell = getCell(row, col) ?: return this
        if (cell.isBlack || cell.userInput == input) return this
        val newGrid = grid.mapIndexed { r, rowCells ->
            if (r == row) {
                rowCells.mapIndexed { c, c2 ->
                    if (c == col) c2.copy(userInput = input) else c2
                }
            } else {
                rowCells
            }
        }
        return copy(grid = newGrid)
    }

    fun withClearedInput(): CrosswordPuzzle {
        val newGrid = grid.map { row ->
            row.map { cell ->
                if (!cell.isBlack && cell.userInput != null) cell.copy(userInput = null) else cell
            }
        }
        return copy(grid = newGrid)
    }

    fun withRevealedClue(clue: Clue): CrosswordPuzzle {
        val clueCells = clue.getCells(grid).associate { Pair(it.row, it.col) to it.answer }
        val newGrid = grid.map { row ->
            row.map { cell ->
                val answer = clueCells[Pair(cell.row, cell.col)]
                if (answer != null) cell.copy(userInput = answer) else cell
            }
        }
        return copy(grid = newGrid)
    }
}
