package com.crossword.crazy.data

import com.crossword.crazy.model.Cell
import com.crossword.crazy.model.Clue
import com.crossword.crazy.model.CrosswordPuzzle
import com.crossword.crazy.model.Direction

object PuzzleProvider {

    fun getSamplePuzzles(): List<CrosswordPuzzle> {
        return listOf(
            createSimplePuzzle(),
            createMediumPuzzle()
        )
    }

    fun getPuzzle(index: Int): CrosswordPuzzle {
        val puzzles = getSamplePuzzles()
        return puzzles[index % puzzles.size]
    }

    private fun createSimplePuzzle(): CrosswordPuzzle {
        val pattern = listOf(
            "CAT",
            "ARE",
            "RED"
        )

        val grid = buildGrid(pattern)

        val clues = listOf(
            Clue(1, "Feline pet", Direction.ACROSS, 0, 0, 3, "CAT"),
            Clue(2, "Exist", Direction.ACROSS, 1, 0, 3, "ARE"),
            Clue(3, "Primary color", Direction.ACROSS, 2, 0, 3, "RED"),
            Clue(1, "Automobile", Direction.DOWN, 0, 0, 3, "CAR"),
            Clue(2, "Present tense of to be", Direction.DOWN, 0, 1, 3, "ARE"),
            Clue(3, "Conclusion", Direction.DOWN, 0, 2, 3, "TED")
        )

        return CrosswordPuzzle("Simple Puzzle", grid, clues)
    }

    private fun createMediumPuzzle(): CrosswordPuzzle {
        val pattern = listOf(
            "TEAM",
            "EACH",
            "ACHE",
            "MHER"
        )

        val grid = buildGrid(pattern)

        val clues = listOf(
            Clue(1, "Group of players", Direction.ACROSS, 0, 0, 4, "TEAM"),
            Clue(2, "Every one", Direction.ACROSS, 1, 0, 4, "EACH"),
            Clue(3, "Pain or hurt", Direction.ACROSS, 2, 0, 4, "ACHE"),
            Clue(4, "M plus her", Direction.ACROSS, 3, 0, 4, "MHER"),
            Clue(1, "Group of players", Direction.DOWN, 0, 0, 4, "TEAM"),
            Clue(2, "Every one", Direction.DOWN, 0, 1, 4, "EACH"),
            Clue(3, "Pain or hurt", Direction.DOWN, 0, 2, 4, "ACHE"),
            Clue(4, "M plus her", Direction.DOWN, 0, 3, 4, "MHER")
        )

        return CrosswordPuzzle("Medium Puzzle", grid, clues)
    }

    private fun buildGrid(pattern: List<String>): List<List<Cell>> {
        val rows = pattern.size
        val cols = pattern.maxOfOrNull { it.length } ?: 0

        val grid = mutableListOf<MutableList<Cell>>()
        val numberMap = mutableMapOf<Pair<Int, Int>, Int>()
        var currentNumber = 1

        for (row in 0 until rows) {
            val rowCells = mutableListOf<Cell>()
            for (col in 0 until cols) {
                val char = pattern[row].getOrNull(col)
                val isBlack = char == null || char == '#'

                var cellNumber: Int? = null
                if (!isBlack) {
                    val needsNumber = (col == 0 || pattern[row].getOrNull(col - 1) == '#') ||
                                     (row == 0 || pattern.getOrNull(row - 1)?.getOrNull(col) == '#')

                    if (needsNumber) {
                        cellNumber = currentNumber
                        numberMap[Pair(row, col)] = currentNumber
                        currentNumber++
                    }
                }

                rowCells.add(
                    Cell(
                        row = row,
                        col = col,
                        answer = if (isBlack) null else char,
                        number = cellNumber
                    )
                )
            }
            grid.add(rowCells)
        }

        return grid
    }
}
