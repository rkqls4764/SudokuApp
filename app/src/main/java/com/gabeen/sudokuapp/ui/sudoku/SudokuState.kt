package com.gabeen.sudokuapp.ui.sudoku

import com.gabeen.sudokuapp.domain.model.Difficulty

data class SudokuState(
    val difficulty: Difficulty? = null,             // 난이도
    val timerState: TimerState = TimerState(),      // 타이머 상태
    val answer: List<List<Int>> = emptyList(),      // 정답
    val cells: List<List<CellState>> = emptyList(), // 스도쿠 칸 상태
    val selectCellIdx: Int? = null,                 // 선택한 칸
    val isMemo: Boolean = false,                    // 메모 모드 여부
    val isFinished: Boolean = false,                // 종료 여부
    val isNewRecord: Boolean = false                // 기록 갱신 여부
) {
    fun isAllCorrect(): Boolean {
        // 아직 게임 시작 안 됐거나 정답 없음
        if (answer.isEmpty() || cells.isEmpty()) return false

        return cells.indices.all { r ->
            cells[r].indices.all { c ->
                val cell = cells[r][c]

                // fixed 칸은 검사 대상 제외
                if (cell.fixed) {
                    true
                } else {
                    cell.value != null && cell.value == answer[r][c]
                }
            }
        }
    }
}

data class CellState(
    val value: Int? = null,         // 입력 값 (1~9 or null)
    val fixed: Boolean = false,     // 문제에서 주어진 값인지 여부
    val memo: Set<Int> = emptySet() // 메모
)

data class TimerState(
    val isRunning: Boolean = false,
    val elapsedMillis: Long = 0L
) {
    val elapsedText: String
        get() {
            val totalSeconds = elapsedMillis / 1000
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60
            val millis = (elapsedMillis % 1000) / 10 // 2자리
            return return String.format("%02d:%02d", minutes, seconds)
//            return return String.format("%02d:%02d:%02d", minutes, seconds, millis)
        }
}