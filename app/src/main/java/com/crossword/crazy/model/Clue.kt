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
            val cell = when (direction) {
                Direction.ACROSS -> grid[startRow][startCol + i]
                Direction.DOWN -> grid[startRow + i][startCol]
            }
            cells.add(cell)
        }
        return cells
    }
}
