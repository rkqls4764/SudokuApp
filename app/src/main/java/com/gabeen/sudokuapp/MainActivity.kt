package com.gabeen.sudokuapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gabeen.sudokuapp.navigation.MainNavGraph
import com.gabeen.sudokuapp.ui.theme.FixedFontScaleTheme
import com.gabeen.sudokuapp.ui.theme.SudokuAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FixedFontScaleTheme {
                SudokuAppTheme {
                    MainNavGraph()
                }
            }
        }
    }
}