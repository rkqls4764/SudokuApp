package com.gabeen.sudokuapp.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.gabeen.sudokuapp.ui.theme.SoftBlack

/* 설정 상단바 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingTopBar(
    title: String,                      // 제목
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
        actions = {
            IconButton(
                onClick = {
                    onClickActIcon()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "액션 버튼",
                    tint = Color.Gray
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}