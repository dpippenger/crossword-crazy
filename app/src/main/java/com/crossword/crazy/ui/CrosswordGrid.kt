package com.crossword.crazy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crossword.crazy.model.Cell
import com.crossword.crazy.model.CrosswordPuzzle

@Composable
fun CrosswordGrid(
    puzzle: CrosswordPuzzle,
    selectedCell: Pair<Int, Int>?,
    onCellClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        for (row in 0 until puzzle.rows) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                for (col in 0 until puzzle.cols) {
                    val cell = puzzle.getCell(row, col)
                    if (cell != null) {
                        CrosswordCell(
                            cell = cell,
                            isSelected = selectedCell == Pair(row, col),
                            onClick = { onCellClick(row, col) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CrosswordCell(
    cell: Cell,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        cell.isBlack -> Color.Black
        isSelected -> Color(0xFFFFEB3B)
        else -> Color.White
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(backgroundColor)
            .border(1.dp, Color.Black)
            .clickable(enabled = !cell.isBlack, onClick = onClick)
            .padding(2.dp)
    ) {
        if (!cell.isBlack) {
            cell.number?.let { number ->
                Text(
                    text = number.toString(),
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(2.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            cell.userInput?.let { input ->
                Text(
                    text = input.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
