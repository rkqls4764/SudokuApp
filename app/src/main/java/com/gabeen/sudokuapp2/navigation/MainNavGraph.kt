package com.gabeen.sudokuapp2.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gabeen.sudokuapp2.R
import com.gabeen.sudokuapp2.ui.ad.BannerAdView
import com.gabeen.sudokuapp2.ui.home.HomeScreen
import com.gabeen.sudokuapp2.ui.sudoku.SudokuScreen
import com.gabeen.sudokuapp2.ui.sudoku.SudokuViewModel
import com.gabeen.sudokuapp2.ui.sudoku.SudokuViewModelFactory
import com.gabeen.sudokuapp2.ui.theme.BgBlue

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val factory = remember { SudokuViewModelFactory(context) }

    val sudokuViewModel: SudokuViewModel = viewModel(factory = factory)

    Scaffold(
        bottomBar = {
            BannerAdView(
                modifier = Modifier.fillMaxWidth(),
                adUnitId = stringResource(R.string.admob_banner_top)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues).fillMaxSize().background(
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
}