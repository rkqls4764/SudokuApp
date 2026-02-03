package com.gabeen.sudokuapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gabeen.sudokuapp.ui.home.HomeScreen
import com.gabeen.sudokuapp.ui.sudoku.SudokuScreen
import com.gabeen.sudokuapp.ui.sudoku.SudokuViewModel
import com.gabeen.sudokuapp.ui.sudoku.SudokuViewModelFactory
import com.gabeen.sudokuapp.ui.theme.BgBlue

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val factory = remember { SudokuViewModelFactory(context) }

    val sudokuViewModel: SudokuViewModel = viewModel(factory = factory)

    Box(
        modifier = Modifier.fillMaxSize().background(
            brush = Brush.verticalGradient(
                colors = listOf(BgBlue, Color.White, Color.White)
            )
        )
    ) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") { HomeScreen(navController, sudokuViewModel) }
            composable("sudoku") { SudokuScreen(navController, sudokuViewModel) }
        }
    }
}