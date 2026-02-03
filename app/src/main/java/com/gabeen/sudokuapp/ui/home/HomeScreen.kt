package com.gabeen.sudokuapp.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gabeen.sudokuapp.domain.model.Difficulty
import com.gabeen.sudokuapp.ui.sudoku.SudokuViewModel
import com.gabeen.sudokuapp.ui.theme.LevelGreen
import com.gabeen.sudokuapp.ui.theme.LevelRed
import com.gabeen.sudokuapp.ui.theme.LevelYellow
import com.gabeen.sudokuapp.ui.theme.SoftBlack

/* 홈 화면 */
@Composable
fun HomeScreen(navController: NavController, sudokuViewModel: SudokuViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 30.dp).padding(top = 120.dp, bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "가벼운 스도쿠",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = SoftBlack
        )

        Spacer(modifier = Modifier.height(220.dp))

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 난이도 3개(비우는 칸 수로 난이도 설정)
            LevelItem(
                text = "쉬움",
                record = sudokuViewModel.getRecord(Difficulty.EASY),
                color = LevelYellow,
                onClick = {
                    sudokuViewModel.startSudoku(blanks = 20, difficulty = Difficulty.EASY)
                    navController.navigate("sudoku")
                }
            )

            LevelItem(
                text = "보통",
                record = sudokuViewModel.getRecord(Difficulty.NORMAL),
                color = LevelGreen,
                onClick = {
                    sudokuViewModel.startSudoku(blanks = 35, difficulty = Difficulty.NORMAL)
                    navController.navigate("sudoku")
                }
            )

            LevelItem(
                text = "어려움",
                record = sudokuViewModel.getRecord(Difficulty.HARD),
                color = LevelRed,
                onClick = {
                    sudokuViewModel.startSudoku(blanks = 50, difficulty = Difficulty.HARD)
                    navController.navigate("sudoku")

                }
            )
        }
    }
}

/* 난이도 아이템 */
@Composable
private fun LevelItem(
    text: String,
    record: String,
    color: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // 버튼 눌림 애니메이션 구현 (y 위치: 눌리면 0, 평소엔 -6)
    val offsetY by animateDpAsState(
        targetValue = if (isPressed) 0.dp else (-6).dp,
        animationSpec = tween(durationMillis = 120),
        label = "cardOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false,
                spotColor = Color.LightGray
            )
            .offset(y = offsetY)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = color, shape = RoundedCornerShape(20.dp))
                .clip(shape = RoundedCornerShape(20.dp))
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(
                        bounded = true,
                        color = Color.White.copy(alpha = 1.0f)
                    ),
                    onClick = onClick
                ),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                color.copy(alpha = 0.01f),
                                color.copy(alpha = 0.1f),
                                color.copy(alpha = 0.4f)
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = text,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 각 레벨 별 최단 기록 출력
                Text(
                    text = record,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = SoftBlack
                )
            }
        }
    }
}