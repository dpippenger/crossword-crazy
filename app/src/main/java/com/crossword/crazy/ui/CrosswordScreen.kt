package com.crossword.crazy.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.crossword.crazy.viewmodel.CheckResult
import com.crossword.crazy.viewmodel.CrosswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrosswordScreen(
    viewModel: CrosswordViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.puzzle?.title ?: "Crossword Crazy") },
                actions = {
                    TextButton(onClick = { viewModel.loadNewPuzzle(0) }) {
                        Text("New Game")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            uiState.puzzle?.let { puzzle ->
                CrosswordGrid(
                    puzzle = puzzle,
                    selectedCell = uiState.selectedCell,
                    onCellClick = { row, col ->
                        viewModel.onCellSelected(row, col)
                        focusRequester.requestFocus()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.4f)
                        .padding(16.dp)
                )

                uiState.selectedClue?.let { clue ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "${clue.number}. ${clue.text}",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.checkAnswers() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Check")
                    }
                    Button(
                        onClick = { viewModel.revealCurrentAnswer() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Reveal")
                    }
                    Button(
                        onClick = { viewModel.clearAll() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear")
                    }
                }

                when (uiState.checkResult) {
                    CheckResult.CORRECT -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text(
                                text = "All answers are correct!",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                    CheckResult.INCORRECT -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = "Some answers are incorrect",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                    CheckResult.NONE -> {}
                }

                CluesList(
                    acrossClues = puzzle.acrossClues,
                    downClues = puzzle.downClues,
                    selectedClue = uiState.selectedClue,
                    onClueClick = { clue ->
                        viewModel.onClueSelected(clue)
                        focusRequester.requestFocus()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.6f)
                )

                TextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier
                        .size(0.dp)
                        .focusRequester(focusRequester)
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown) {
                                when {
                                    keyEvent.key == Key.Backspace || keyEvent.key == Key.Delete -> {
                                        viewModel.onBackspace()
                                        true
                                    }
                                    keyEvent.key.keyCode in Key.A.keyCode..Key.Z.keyCode -> {
                                        val char = Char(('A'.code + (keyEvent.key.keyCode - Key.A.keyCode)).toInt())
                                        viewModel.onLetterInput(char)
                                        true
                                    }
                                    else -> false
                                }
                            } else {
                                false
                            }
                        }
                )
            }
        }

        if (uiState.showCompletionDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissCompletionDialog() },
                title = { Text("Congratulations!") },
                text = { Text("You've completed the puzzle correctly!") },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissCompletionDialog() }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
