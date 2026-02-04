package com.gabeen.sudokuapp.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gabeen.sudokuapp.ui.theme.BasicBlue

/* 눌림 여부 체크 아이콘 버튼 */
@Composable
fun PressCheckIconButton(icon: ImageVector, name: String, isPressed: Boolean, onClick: () -> Unit) {
    val color = if (isPressed) BasicBlue else Color.Gray
    val rotation by animateFloatAsState(
        targetValue = if (isPressed) 0f else 45f
    )

    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(1.dp, color)
    ) {
        Row(
            modifier = Modifier.wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = color,
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotation
                }
            )

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = color,
                maxLines = 1
            )

            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

/* 아이콘 버튼 */
@Composable
fun BasicIconButton(icon: ImageVector, name: String, onClick: () -> Unit) {
    val color = Color.Gray

    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(1.dp, color)
    ) {
        Row(
            modifier = Modifier.wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = color
            )

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = color,
                maxLines = 1
            )

            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

/* 기본 버튼 */
@Composable
fun BasicButton(name: String, wrapContent: Boolean = false, onClick: () -> Unit) {
    Button(
        modifier = Modifier.height(40.dp).then(if (wrapContent) Modifier.wrapContentWidth() else Modifier.fillMaxWidth()),
        contentPadding = if (wrapContent) PaddingValues(horizontal = 12.dp) else PaddingValues(0.dp),
        onClick = { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BasicBlue,
            contentColor = Color.White
        )
    ) {
        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}