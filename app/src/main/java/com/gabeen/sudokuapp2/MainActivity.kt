package com.gabeen.sudokuapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gabeen.sudokuapp2.navigation.MainNavGraph
import com.gabeen.sudokuapp2.ui.theme.FixedFontScaleTheme
import com.gabeen.sudokuapp2.ui.theme.SudokuAppTheme
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
        setContent {
            FixedFontScaleTheme {
                SudokuAppTheme {
                    MainNavGraph()
                }
            }
        }
    }
}