package com.crossword.crazy.model

data class Cell(
    val row: Int,
    val col: Int,
    val answer: Char?,
    val number: Int? = null,
    var userInput: Char? = null,
    val isBlack: Boolean = answer == null
) {
    val isEmpty: Boolean
        get() = !isBlack && userInput == null

    val isCorrect: Boolean
        get() = !isBlack && userInput != null && userInput == answer

    fun clear() {
        userInput = null
    }
}
