package com.example.quizizz_app.activities.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonItem(
    modifier: Modifier = Modifier,
    nameButton: String,
    textColor: Color,
    gradient: Brush,
    shadowColor: Color,
    paddingButton: Dp,
    shadowBottomOffset: Float,
    buttonHeight: Float = 60f,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    onClick: () -> Unit,
) {

    Box(
        modifier = modifier
            .padding(horizontal = paddingButton)
            .clip(shape = shape)
            .background(color = shadowColor, shape = shape)
            .clickable { }
            .fillMaxWidth()
            .height(buttonHeight.dp)

    ) {
        // Button
        Box(
            modifier = modifier
                .padding(top = 1.dp, start = 1.dp, end = 1.dp, bottom = shadowBottomOffset.dp)
                .clip(shape = shape)
                .background(gradient, shape = shape)
                .fillMaxWidth()
                .clickable { onClick() }
                .height(buttonHeight.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = nameButton,
                style = MaterialTheme.typography.displayMedium.copy(
                    color = textColor,
                    fontSize = 35.sp
                )
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedCounter2(
    point: Int,
    imageResId: Int, // ID của hình ảnh trong resource
    title: String,
    modifier: Modifier = Modifier,
) {

    // Giao diện hiển thị
    Column(
        modifier = Modifier
            .border(3.dp, Color(0xff6342e8), shape = RoundedCornerShape(10.dp))
            .height(150.dp)
            .width(150.dp)
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hiển thị hình ảnh
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier.size(60.dp).fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth(),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 19.sp,
            ),
            color = Color.White
        )
        Spacer(modifier = Modifier.padding(2.dp))
        AnimatedContent(
            targetState = point,
            transitionSpec = {
                slideInVertically { it } with slideOutVertically { it }
            }, label = ""
        ) { point ->
            Text(
                text = point.toString(), // Hiển thị giá trị animatedPoint
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 19.sp,
                ),
                color = Color.White
            )
        }

    }
}