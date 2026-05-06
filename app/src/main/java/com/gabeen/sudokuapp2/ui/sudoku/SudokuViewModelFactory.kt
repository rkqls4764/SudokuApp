package com.gabeen.sudokuapp2.ui.sudoku

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gabeen.sudokuapp2.data.preference.RecordPreferences

class SudokuViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SudokuViewModel::class.java)) {
            val prefs = RecordPreferences(context)
            return SudokuViewModel(prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}