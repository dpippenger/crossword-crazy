package com.crossword.crazy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crossword.crazy.ui.CrosswordScreen
import com.crossword.crazy.ui.theme.CrosswordCrazyTheme
import com.crossword.crazy.viewmodel.CrosswordViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CrosswordCrazyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: CrosswordViewModel = viewModel()
                    CrosswordScreen(viewModel = viewModel)
                }
            }
        }
    }
}
