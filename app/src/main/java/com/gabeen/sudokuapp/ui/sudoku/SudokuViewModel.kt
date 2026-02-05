package com.gabeen.sudokuapp.ui.sudoku

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabeen.sudokuapp.data.preference.RecordPreferences
import com.gabeen.sudokuapp.domain.model.Difficulty
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SudokuViewModel(private val recordPreferences: RecordPreferences): ViewModel() {
    private val _sudokuState = MutableStateFlow(SudokuState())
    val sudokuState = _sudokuState.asStateFlow()

    private var tickerJob: Job? = null

    /* 초기화 (비울 칸 개수) */
    fun startSudoku(blanks: Int, difficulty: Difficulty) {
        tickerJob?.cancel()
        tickerJob = null

        val rnd = java.util.Random()

        // 정답 보드 생성
        val answer = generateSolvedBoard(rnd)

        // 정답에서 blanks개 만큼 0으로 비우기
        val puzzle = answer.map { it.toMutableList() }.toMutableList()

        val positions = (0 until 81).toMutableList()
        java.util.Collections.shuffle(positions, rnd)

        val removeCount = blanks.coerceIn(0, 81)
        for (i in 0 until removeCount) {
            val pos = positions[i]
            val r = pos / 9
            val c = pos % 9
            puzzle[r][c] = 0
        }

        // 채워야할 숫자 수
        val remainingCount = IntArray(10) { 9 }

        // CellState로 변환 + fixed 설정
        val cells: List<List<CellState>> =
            puzzle.map { row ->
                row.map { v ->
                    if (v != 0) remainingCount[v]--
                    CellState(
                        value = if (v == 0) null else v,
                        fixed = v != 0
                    )
                }
            }

        _sudokuState.value = SudokuState(
            difficulty = difficulty,
            timerState = TimerState(),
            answer = answer,
            cells = cells,
            remainingCount = remainingCount.toList(),
            isFinished = false,
            isNewRecord = false
        )

        // 타이머 실행
        startTimer()
    }

    /* 정답 보드 생성 */
    private fun generateSolvedBoard(rnd: java.util.Random): List<List<Int>> {
        val base = 3
        val side = base * base

        fun pattern(r: Int, c: Int): Int = (base * (r % base) + r / base + c) % side

        fun shuffled(list: List<Int>): List<Int> =
            list.toMutableList().also { java.util.Collections.shuffle(it, rnd) }

        val rBase = (0 until base).toList()
        val rows = shuffled(rBase).flatMap { g -> shuffled(rBase).map { r -> g * base + r } }
        val cols = shuffled(rBase).flatMap { g -> shuffled(rBase).map { c -> g * base + c } }
        val nums = shuffled((1..side).toList())

        return rows.map { r ->
            cols.map { c ->
                nums[pattern(r, c)]
            }
        }
    }

    /* 입력 칸 선택 */
    fun selectCell(idx: Int) {
        _sudokuState.update { it.copy(selectCellIdx = idx) }
    }

    /* 입력 칸 초기화 */
    fun initSelectedCell() {
        _sudokuState.update { it.copy(selectCellIdx = null) }
    }

    /* 숫자 입력 */
    fun inputNum(value: Int) {
        val state = _sudokuState.value
        val idx = state.selectCellIdx

        // 선택된 칸이 없으면 무시
        if (idx == null) return

        val row = idx / 9
        val col = idx % 9
        val targetCell = state.cells[row][col]

        // 고정 칸은 수정 불가
        if (targetCell.fixed) return

        // 값이 있는 상태에서 메모 수정 불가
        if (state.isMemo && targetCell.value != null) return

        val newRemaining = state.remainingCount.toMutableList()

        val newCells = if (state.isMemo) {
            state.cells.mapIndexed { r, rowList ->
                if (r != row) {
                    rowList
                } else {
                    rowList.mapIndexed { c, cell ->
                        if (c != col) {
                            cell
                        } else {
                            // 이미 입력한 값과 같은 값 입력이 들어오면 지우기
                            if (cell.memo.contains(value)) {
                                cell.copy(memo = cell.memo - value)
                            } else {
                                cell.copy(memo = cell.memo + value)
                            }
                        }
                    }
                }
            }
        } else {
            state.cells.mapIndexed { r, rowList ->
                if (r != row) {
                    rowList
                } else {
                    rowList.mapIndexed { c, cell ->
                        if (c != col) {
                            cell
                        } else {
                            // 이미 입력한 값과 같은 값 입력이 들어오면 지우기
                            if (cell.value == value) {
                                newRemaining[value]++
                                cell.copy(value = null)
                            } else {
                                if (cell.value != null) {
                                    newRemaining[cell.value]++
                                }
                                newRemaining[value]--
                                cell.copy(value = value)
                            }
                        }
                    }
                }
            }
        }

        _sudokuState.value = state.copy(cells = newCells, remainingCount = newRemaining)
    }

    /* 메모 모드 변경 */
    fun changeMemoMode() {
        _sudokuState.update { it.copy(isMemo = !it.isMemo) }
    }

    /* 한 칸 숫자, 메모 지우기 */
    fun deleteNum() {
        val state = _sudokuState.value
        val idx = state.selectCellIdx

        // 선택된 칸이 없으면 무시
        if (idx == null) return

        val row = idx / 9
        val col = idx % 9
        val targetCell = state.cells[row][col]

        // 고정 칸은 수정 불가
        if (targetCell.fixed) return

        val newRemaining = state.remainingCount.toMutableList()
        if (targetCell.value != null) {
            newRemaining[targetCell.value]++
        }

        val newCells = state.cells.mapIndexed { r, rowList ->
            if (r != row) {
                rowList
            } else {
                rowList.mapIndexed { c, cell ->
                    if (c != col) {
                        cell
                    } else {
                        cell.copy(value = null, memo = emptySet())
                    }
                }
            }
        }

        _sudokuState.value = state.copy(cells = newCells, remainingCount = newRemaining)
    }

    /* 전체 숫자, 메모 초기화 */
    fun reset() {
        val state = sudokuState.value
        val cells = state.cells
        val newRemaining = MutableList(10) { 9 }

        val newCells = cells.map { row ->
            row.map { cell ->
                if (!cell.fixed) {
                    CellState()
                } else {
                    newRemaining[cell.value!!]--
                    cell
                }
            }
        }

        _sudokuState.update { it.copy(cells = newCells, selectCellIdx = null, remainingCount = newRemaining, isMemo = false) }
    }

    /* 종료 */
    fun finish() {
        pauseTimer()

        val state = _sudokuState.value
        val difficulty = state.difficulty ?: return

        // 전부 맞히면 최단 기록 갱신
        var isNew = false
        if (state.isAllCorrect()) {
            isNew = recordPreferences.saveRecord(difficulty, state.timerState.elapsedMillis)
        }

        _sudokuState.update { it.copy(isFinished = true, isNewRecord = isNew) }
    }

    /* 기록 조회 */
    fun getRecord(difficulty: Difficulty): String {
        val ms = recordPreferences.getRecord(difficulty)
        if (ms == Long.MAX_VALUE) return "-- : -- : --"

        val min = ms / 60000
        val sec = (ms % 60000) / 1000
        val milli = (ms % 1000) / 10

        return "%02d:%02d:%02d".format(min, sec, milli)
    }

    /* 타이머 실행 */
    fun startTimer() {
        // 이미 실행 중이면 중복 시작 방지
        if (_sudokuState.value.timerState.isRunning) return

        _sudokuState.update { it.copy(timerState = it.timerState.copy(isRunning = true)) }

        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            while (true) {
                delay(10)
                _sudokuState.update { state ->
                    val t = state.timerState

                    if (!t.isRunning) {
                        state
                    } else {
                        state.copy(
                            timerState = t.copy(elapsedMillis = t.elapsedMillis + 10)
                        )
                    }
                }
            }
        }
    }

    /* 타이머 정지 */
    fun pauseTimer() {
        _sudokuState.update { it.copy(timerState = it.timerState.copy(isRunning = false)) }
        tickerJob?.cancel()
        tickerJob = null
    }
}