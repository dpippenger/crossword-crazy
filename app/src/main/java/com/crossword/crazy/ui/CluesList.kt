package com.crossword.crazy.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.crossword.crazy.model.Clue
import com.crossword.crazy.model.Direction

@Composable
fun CluesList(
    acrossClues: List<Clue>,
    downClues: List<Clue>,
    selectedClue: Clue?,
    onClueClick: (Clue) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = modifier) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Across") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
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
        items(clues) { clue ->
            ClueItem(
                clue = clue,
                isSelected = clue == selectedClue,
                onClick = { onClueClick(clue) }
            )
        }
    }
}

@Composable
fun ClueItem(
    clue: Clue,
    isSelected: Boolean,
    onClick: () -> Unit
) {
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
