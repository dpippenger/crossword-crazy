package com.crossword.crazy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        (0 until puzzle.rows).forEach { row ->
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                (0 until puzzle.cols).forEach { col ->
                    val cell = puzzle.getCell(row, col)
                    if (cell != null) {
                        CrosswordCell(
                            cell = cell,
                            isSelected = selectedCell == Pair(row, col),
                            row = row,
                            col = col,
                            onCellClick = onCellClick,
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
    row: Int,
    col: Int,
    onCellClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        cell.isBlack -> MaterialTheme.colorScheme.onSurface
        isSelected -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.surface
    }

    val onClick = remember(row, col, onCellClick) { { onCellClick(row, col) } }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(backgroundColor)
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
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
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            cell.userInput?.let { input ->
                Text(
                    text = input.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
