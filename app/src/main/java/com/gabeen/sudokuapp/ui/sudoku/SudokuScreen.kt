package com.gabeen.sudokuapp.ui.sudoku

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gabeen.sudokuapp.ui.component.BasicButton
import com.gabeen.sudokuapp.ui.component.BasicIconButton
import com.gabeen.sudokuapp.ui.component.HomeTopBar
import com.gabeen.sudokuapp.ui.component.PressCheckIconButton
import com.gabeen.sudokuapp.ui.theme.BasicBlue
import com.gabeen.sudokuapp.ui.theme.BgBlue
import com.gabeen.sudokuapp.ui.theme.FixedCellBg
import com.gabeen.sudokuapp.ui.theme.FixedNumberColor
import com.gabeen.sudokuapp.ui.theme.LevelGreen
import com.gabeen.sudokuapp.ui.theme.LevelGreenBg
import com.gabeen.sudokuapp.ui.theme.LevelRed
import com.gabeen.sudokuapp.ui.theme.LevelRedBg
import com.gabeen.sudokuapp.ui.theme.SoftBlack

/* 스도쿠 화면 */
@Composable
fun SudokuScreen(navController: NavController, sudokuViewModel: SudokuViewModel) {
    val sudokuState by sudokuViewModel.sudokuState.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            HomeTopBar(
                title = sudokuState.timerState.elapsedText, // 타이머 출력
                onClickActIcon = { navController.popBackStack() }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp).padding(bottom = 10.dp)
            ) {
                BasicButton(
                    name = if (!sudokuState.isFinished) "제출하기" else "홈으로 돌아가기",
                    onClick = { if (!sudokuState.isFinished) sudokuViewModel.finish() else navController.popBackStack() }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures(onTap = { sudokuViewModel.initSelectedCell() }) }
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                SudokuBoard(
                    cells = sudokuState.cells,
                    answer = sudokuState.answer,
                    selectedCellIdx = sudokuState.selectCellIdx,
                    isFinished = sudokuState.isFinished,
                    onSelectIdx = { sudokuViewModel.selectCell(it) }
                )

                if (!sudokuState.isFinished) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        BasicIconButton(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Refresh,
                            name = "초기화",
                            onClick = { sudokuViewModel.reset() }
                        )

                        BasicIconButton(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Clear,
                            name = "지우기",
                            onClick = { sudokuViewModel.deleteNum() }
                        )

                        PressCheckIconButton(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Edit,
                            name = "메모하기",
                            isPressed = sudokuState.isMemo,
                            onClick = { sudokuViewModel.changeMemoMode() }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    NumberPad(
                        remainingCount = sudokuState.remainingCount,
                        onClick = { sudokuViewModel.inputNum(it) }
                    )
                }

                if (sudokuState.isNewRecord) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 70.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "기록이 갱신되었습니다!",
                            color = SoftBlack,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

/* 스도쿠 판 */
@Composable
private fun SudokuBoard(
    cells: List<List<CellState>>,
    answer: List<List<Int>>,
    selectedCellIdx: Int?,
    isFinished: Boolean,
    onSelectIdx: (Int) -> Unit
) {
    if (cells.isEmpty()) return

    val cellFlat = remember(cells) { cells.flatten() }
    val answerFlat = remember(answer) { answer.flatten()}

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f)
    ) {
        val boardSize = maxWidth
        val cellSize = boardSize / 9

        Box(modifier = Modifier.size(boardSize)) {

            LazyVerticalGrid(
                modifier = Modifier.size(boardSize),
                columns = GridCells.Fixed(9),
                userScrollEnabled = false
            ) {
                items(81) { index ->
                    Box(modifier = Modifier.size(cellSize)) {
                        SudokuCell(
                            cellState = cellFlat[index],
                            answer = answerFlat[index],
                            isSelect = selectedCellIdx == index || (selectedCellIdx != null && cellFlat[selectedCellIdx].value != null && cellFlat[index].value == cellFlat[selectedCellIdx].value),
                            isFinished = isFinished,
                            onClick = { onSelectIdx(index) }
                        )
                    }
                }
            }

            Canvas(modifier = Modifier.matchParentSize()) {
                val thin = 1.dp.toPx()
                val thick = 3.dp.toPx()

                val w = size.width
                val h = size.height
                val cell = w / 9f

                for (i in 0..9) {
                    val x = i * cell
                    val stroke = if (i % 3 == 0) thick else thin
                    drawLine(
                        color = Color.Gray,
                        start = Offset(x, 0f),
                        end = Offset(x, h),
                        strokeWidth = stroke
                    )
                }

                for (i in 0..9) {
                    val y = i * cell
                    val stroke = if (i % 3 == 0) thick else thin
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = stroke
                    )
                }
            }
        }
    }
}

/* 스도쿠 칸 */
@Composable
private fun SudokuCell(
    cellState: CellState,
    answer: Int,
    isSelect: Boolean,
    isFinished: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isFinished) {
        if (cellState.fixed) FixedCellBg else if (cellState.value == answer) LevelGreenBg else LevelRedBg
    } else {
        if (isSelect) BgBlue else if (cellState.fixed) FixedCellBg else Color.White
    }

    val fontColor = if (isFinished) {
        if (cellState.fixed) FixedNumberColor else if (cellState.value == answer) LevelGreen else LevelRed
    } else {
        if (cellState.fixed) FixedNumberColor else BasicBlue
    }

    val fontWeight = if (cellState.fixed) FontWeight.Medium else FontWeight.Bold

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // 입력한 숫자가 없으면 메모 출력
        if (cellState.value == null) {
            NotesGrid(
                notes = cellState.memo
            )
        } else {
            Text(
                text = cellState.value.toString(),
                fontSize = 24.sp,
                fontWeight = fontWeight,
                color = fontColor
            )
        }
    }
}

/* 메모 칸 */
@Composable
private fun NotesGrid(
    notes: Set<Int>
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        for (row in 0 until 3) {
            Row(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (col in 0 until 3) {
                    val number = row * 3 + col + 1

                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (number in notes) {
                            Text(
                                text = number.toString(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = SoftBlack,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

/* 숫자 입력 바 */
@Composable
private fun NumberPad(remainingCount: List<Int>, onClick: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        for (num in 1..9) {
            NumberButton(
                num = num,
                cnt = remainingCount[num],
                onClick = { onClick(num) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/* 숫자 버튼 */
@Composable
private fun NumberButton(
    num: Int,
    cnt: Int,
    onClick: () -> Unit,
    modifier: Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // 버튼 눌림 애니메이션 구현
    val offsetY by animateDpAsState(
        targetValue = if (isPressed) 0.dp else (-2).dp,
        animationSpec = tween(durationMillis = 120),
        label = "cardOffset"
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false,
                spotColor = Color.LightGray
            )
            .offset(y = offsetY)
    ) {
        Card(
            modifier = modifier,
            border = BorderStroke(width = 1.dp, color = BasicBlue),
            interactionSource = interactionSource,
            onClick = onClick
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(horizontal = 4.dp)
                    .padding(top = 10.dp, bottom = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = num.toString(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = SoftBlack
                )

                Text(
                    text = cnt.toString(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}