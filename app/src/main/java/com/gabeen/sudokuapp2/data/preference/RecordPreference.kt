package com.gabeen.sudokuapp2.data.preference

import android.content.Context
import com.gabeen.sudokuapp2.domain.model.Difficulty

private const val PREF_NAME = "sudoku_record"

class RecordPreferences(context: Context) {
    private val prefs =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getRecord(difficulty: Difficulty): Long {
        return prefs.getLong(difficulty.key, Long.MAX_VALUE)
    }

    fun saveRecord(difficulty: Difficulty, timeMillis: Long): Boolean {
        val prev = getRecord(difficulty)

        return if (timeMillis < prev) {
            prefs.edit().putLong(difficulty.key, timeMillis).apply()
            true
        } else {
            false
        }
    }
}