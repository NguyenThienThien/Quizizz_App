package com.example.quizizz_app.activities.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizizz_app.R

@Composable
fun StepProgressBarItem(
    currentQuestionIndex: Int,
    totalQuestions: Int,
    correctStreak: Int,  // Số câu trả lời đúng liên tiếp
    modifier: Modifier = Modifier
) {
    // Tính toán tỷ lệ hoàn thành
    val progress = currentQuestionIndex.toFloat() / totalQuestions.toFloat()

    // Animation cho tỷ lệ hoàn thành
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600), label = "" // Đặt thời gian mượt mà
    )

    // Chọn màu dựa trên số câu trả lời đúng liên tiếp
    val gradientBrush = when {
        correctStreak >= 10 -> Brush.linearGradient(listOf(Color(0xffFFD700), Color(0xffFFA500)))  // Vàng cam sau 10 câu đúng liên tiếp
        correctStreak >= 5 -> Brush.linearGradient(listOf(Color(0xff00FF00), Color(0xff32CD32)))   // Xanh lá sau 5 câu đúng liên tiếp
        else -> Brush.linearGradient(listOf(Color(0xff7F77FE), Color(0xffA573FF)))  // Màu ban đầu
    }

    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(40.dp)
    ) {
        // Thanh tiến trình
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .fillMaxWidth()
                .height(40.dp)
                .background(Color(0xff353a75))
        ) {
            // Thanh tiến trình đã hoàn thành
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .fillMaxWidth(fraction = animatedProgress)
                    .height(40.dp)
                    .background(gradientBrush),  // Sử dụng màu gradient được chọn
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "${currentQuestionIndex + 1}",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .align(Alignment.CenterEnd),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    ),
                    color = Color.White
                )
            }
        }

        // Lá cờ trắng, đè lên thanh tiến trình ở cuối
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(CircleShape)
                .size(40.dp)
                .background(
                    Brush.linearGradient(listOf(Color(0xff7F77FE), Color(0xffA573FF)))
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.flag_white),
                contentDescription = "Icon Flag White"
            )
        }
    }
}
