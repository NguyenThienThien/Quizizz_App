package com.example.quizizz_app.activities.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizizz_app.models.AnswerState

@Composable
fun AnswerButton(
    answerText: String,
    onClick: () -> Unit,
    isQuizChecked: Boolean,
    answerState: AnswerState
) {

    val borderColor = getBackgroundColor(answerState)
    val iconImage: ImageVector? = when (answerState) {
        AnswerState.CORRECT -> Icons.Default.Check
        AnswerState.INCORRECT -> Icons.Default.Close
        AnswerState.UNSELECTED, AnswerState.SELECTED, AnswerState.REVEALED_CORRECT, AnswerState.REVEALED_UNSELECTED -> null
    }
    val iconColor = when (answerState) {
        AnswerState.CORRECT -> Color(0xff46D688)
        AnswerState.INCORRECT -> Color(0xff9E3332)
        else -> Color.Gray
    }
    val backgroundColor = when (answerState) {
        AnswerState.CORRECT -> Color.Transparent
        AnswerState.REVEALED_CORRECT -> Color(0xff46D688).copy(alpha = 0.3f)
        AnswerState.REVEALED_UNSELECTED -> Color(0xffEAECF3)
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(BorderStroke(2.dp, borderColor), shape = RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = answerText,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = if (isQuizChecked) 20.sp else 17.sp // Tăng kích thước font nếu đã chọn
                ),
                color = Color(0xff111245)
            )
        }
        iconImage?.let { imageVector ->
            Icon(
                imageVector = imageVector,
                contentDescription = "Answer icon",
                tint = iconColor, // Hoặc thay đổi tint theo ý muốn
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 8.dp)
            )
        }
    }
}

fun getBackgroundColor(answerState: AnswerState): Color {
    return when (answerState) {
        AnswerState.UNSELECTED -> Color(0xFFCFD0E4)
        AnswerState.SELECTED -> Color(0xFF4553B7)
        AnswerState.CORRECT -> Color(0xFF46D688)
        AnswerState.INCORRECT -> Color(0xFF9E3332)
        AnswerState.REVEALED_CORRECT -> Color.Transparent
        AnswerState.REVEALED_UNSELECTED -> Color.Transparent
    }
}

@Composable
fun CustomBigButton(
    onClick: () -> Unit = {},
    title: String
){
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff005595)) // Color: 312064
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}