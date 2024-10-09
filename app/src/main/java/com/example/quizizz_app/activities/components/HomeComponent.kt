package com.example.quizizz_app.activities.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SeeResult(
    point: String,
    imageResId: Int,
    title: String,
    modifier: Modifier = Modifier,
) {
    // Giao diện hiển thị
    Column(
        modifier = modifier
            .border(1.dp, Color(0xff6342e8), shape = RoundedCornerShape(10.dp))
            .height(200.dp) // Chiều cao cố định
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hiển thị hình ảnh
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            ),
            color = Color.Black
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = point.toString(), // Hiển thị giá trị animatedPoint
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
            ),
            color = Color.Black
        )
    }
}