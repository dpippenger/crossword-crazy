package com.crossword.crazy.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import com.crossword.crazy.model.Clue
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

    val onCellClick = remember(viewModel, focusRequester) { { row: Int, col: Int ->
        viewModel.onCellSelected(row, col)
        focusRequester.requestFocus()
    } }

    val onClueClick = remember(viewModel, focusRequester) { { clue: Clue ->
        viewModel.onClueSelected(clue)
        focusRequester.requestFocus()
    } }

    val onNewGameClick = remember(viewModel) { { viewModel.loadNewPuzzle(0) } }
    val onCheckAnswersClick = remember(viewModel) { { viewModel.checkAnswers() } }
    val onRevealAnswerClick = remember(viewModel) { { viewModel.revealCurrentAnswer() } }
    val onClearAllClick = remember(viewModel) { { viewModel.clearAll() } }
    val onDismissCompletionDialog = remember(viewModel) { { viewModel.dismissCompletionDialog() } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.puzzle?.title ?: "Crossword Crazy") },
                actions = {
                    TextButton(onClick = onNewGameClick) {
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
                    onCellClick = onCellClick,
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
                        onClick = onCheckAnswersClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Check")
                    }
                    Button(
                        onClick = onRevealAnswerClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Reveal")
                    }
                    Button(
                        onClick = onClearAllClick,
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
                    onClueClick = onClueClick,
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
                                    keyEvent.utf16CodePoint != 0 -> {
                                        val char = keyEvent.utf16CodePoint.toChar()
                                        if (char.isLetter()) {
                                            viewModel.onLetterInput(char.uppercaseChar())
                                            true
                                        } else {
                                            false
                                        }
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
                onDismissRequest = onDismissCompletionDialog,
                title = { Text("Congratulations!") },
                text = { Text("You've completed the puzzle correctly!") },
                confirmButton = {
                    TextButton(onClick = onDismissCompletionDialog) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
