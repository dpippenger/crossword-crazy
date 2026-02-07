package com.crossword.crazy.model

data class Clue(
    val number: Int,
    val text: String,
    val direction: Direction,
    val startRow: Int,
    val startCol: Int,
    val length: Int,
    val answer: String
) {
    fun getCells(grid: List<List<Cell>>): List<Cell> {
        val cells = mutableListOf<Cell>()
        for (i in 0 until length) {
            val row = if (direction == Direction.DOWN) startRow + i else startRow
            val col = if (direction == Direction.ACROSS) startCol + i else startCol

            val cell = grid.getOrNull(row)?.getOrNull(col)
            if (cell != null) {
                cells.add(cell)
            } else {
                // Clue definition is malformed (out of bounds). Stop processing.
                break
            }
        }
        return cells
    }
}
