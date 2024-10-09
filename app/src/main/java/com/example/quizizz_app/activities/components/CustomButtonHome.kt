package com.example.quizizz_app.activities.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizizz_app.R

@Composable
fun ButtonItem(
    modifier: Modifier = Modifier,
    titleButton: String,
    textColor: Color,
    icon: Painter,
    gradient: Brush,
    shadowColor: Brush,
    shadowBottomOffset: Float,
    buttonHeight: Float = 100f,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    onClick: () -> Unit,
) {
    val gradientCircleIconItem = Brush.horizontalGradient(
        colors = listOf(
            Color.White,
            Color.White
        )
    )

    Box(
        modifier = modifier
            .clip(shape = shape)
            .background(brush = shadowColor, shape = shape)
            .fillMaxWidth()
            .height(buttonHeight.dp + shadowBottomOffset.dp)

    ) {
        // Button
        Box(
            modifier = modifier
                .padding(top = 2.dp, start = 2.dp, end = 2.dp, bottom = shadowBottomOffset.dp)
                .clip(shape = shape)
                .background(gradient, shape = shape)
                .fillMaxWidth()
                .clickable { onClick() }
                .height(buttonHeight.dp)
        ) {
            Row(
                modifier = modifier
                    .padding(horizontal = 24.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                CircleIconItem(
                    icon = icon,
                    size = (buttonHeight * 0.7f).dp,
                    color = gradientCircleIconItem,
                    elevationShadow = 0.dp
                )
                Column(
                    modifier = modifier
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.Start

                ) {
                    Text(
                        text = titleButton,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 24.sp
                        ),
                        color = textColor,
                        textAlign = TextAlign.Start
                    )
                }
            }

        }
    }
}

@Composable
fun BottomItem(
    modifier: Modifier,
    onClick: () -> Unit,
    name: String,
    textColor: Color,
    gradientColor: Brush,
) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(
                color = Color(0xff9E3332),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Box(
            modifier = modifier
                .padding(
                    top = 2.dp,
                    start = 2.dp,
                    end = 2.dp,
                    bottom = 5.dp
                )
                .background(gradientColor, shape = RoundedCornerShape(8.dp))
                .fillMaxSize()
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium.copy(color = textColor),
            )
        }
    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ButtonItemPreview(modifier: Modifier = Modifier) {
        val gradientButtonLogin = Brush.horizontalGradient(
            colors = listOf(
                Color(0xffD54646),
                Color(0xffFF9064)
            )
        )
        val gradientTest = Brush.horizontalGradient(
            colors = listOf(
                Color(0xff9E3332),
                Color(0xff9E3332)
            )
        )
        Column {
            ButtonItem(
                titleButton = "Học tập",
                textColor = Color.White,
                icon = painterResource(id = R.drawable.learning),
                gradient = gradientButtonLogin,
                shadowColor = gradientTest,
                shadowBottomOffset = 8f
            ) {

            }
        }

    }


@Composable
fun CustomButtonItem(
    modifier: Modifier = Modifier,
    nameButton: String,
    textColor: Color,
    gradient: Brush,
    shadowColor: Color,
    shadowBottomOffset: Float,
    buttonHeight: Float = 60f,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    onClick: () -> Unit,
) {

    Box(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp)
            .background(color = shadowColor, shape = shape)
            .clickable { }
            .height(buttonHeight.dp)

    ) {
        Box(
            modifier = modifier
                .padding(top = 1.dp, start = 1.dp, end = 1.dp, bottom = shadowBottomOffset.dp)
                .background(gradient, shape = shape)
                .fillMaxWidth()
                .clickable { onClick() }
                .height(buttonHeight.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = nameButton,
                style = MaterialTheme.typography.displayMedium.copy(
                    color = textColor
                )
            )
        }
    }
}

@Composable
fun CircleIconItem(
    modifier: Modifier = Modifier,
    icon: Painter,
    size: Dp,
    color: Brush,
    elevationShadow: Dp,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(height = size, width = size)
            .background(brush = color)
            .shadow(elevation = elevationShadow, shape = CircleShape, clip = true),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = modifier.size(size * 0.5f),
            painter = icon,
            contentDescription = "Icon Circle"
        )
    }
}


