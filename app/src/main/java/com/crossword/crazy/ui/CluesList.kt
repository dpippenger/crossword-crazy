package com.crossword.crazy.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.crossword.crazy.model.Clue

@Composable
fun CluesList(
    acrossClues: List<Clue>,
    downClues: List<Clue>,
    selectedClue: Clue?,
    onClueClick: (Clue) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val onSelectAcross = remember { { selectedTab = 0 } }
    val onSelectDown = remember { { selectedTab = 1 } }

    Column(modifier = modifier) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = onSelectAcross,
                text = { Text("Across") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = onSelectDown,
                text = { Text("Down") }
            )
        }

        when (selectedTab) {
            0 -> CluesColumn(
                clues = acrossClues,
                selectedClue = selectedClue,
                onClueClick = onClueClick
            )
            1 -> CluesColumn(
                clues = downClues,
                selectedClue = selectedClue,
                onClueClick = onClueClick
            )
        }
    }
}

@Composable
fun CluesColumn(
    clues: List<Clue>,
    selectedClue: Clue?,
    onClueClick: (Clue) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(clues, key = { clue -> "${clue.number}-${clue.direction}" }) { clue ->
            ClueItem(
                clue = clue,
                isSelected = clue == selectedClue,
                onClueClick = onClueClick
            )
        }
    }
}

@Composable
fun ClueItem(
    clue: Clue,
    isSelected: Boolean,
    onClueClick: (Clue) -> Unit
) {
    val onClick = remember(clue, onClueClick) { { onClueClick(clue) } }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "${clue.number}.",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(40.dp)
            )
            Text(
                text = clue.text,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
