package com.gabeen.sudokuapp2.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.gabeen.sudokuapp2.ui.theme.BasicBlue
import com.gabeen.sudokuapp2.ui.theme.SoftBlack

/* 홈 상단바 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    title: String,                      // 제목
    showHintIcon: Boolean,              // 힌트 아이콘 출력 여부
    onClickHintIcon: () -> Unit = {},   // 힌트 아이콘 클릭 이벤트
    onClickActIcon: () -> Unit = {}     // 액션 아이콘 클릭 이벤트
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                color = SoftBlack
            )
        },
        navigationIcon = {
            if (showHintIcon) {
                IconButton(
                    onClick = {
                        onClickHintIcon()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "힌트 버튼",
                        tint = BasicBlue
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = {
                    onClickActIcon()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "홈 버튼",
                    tint = Color.Gray
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}