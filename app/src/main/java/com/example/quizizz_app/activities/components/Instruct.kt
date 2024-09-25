package com.example.quizizz_app.activities.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quizizz_app.R

@Composable
fun InstructUps(modifier: Modifier, textInstruct: String, contentInstruct: String) {
    Row {

        CircleAvatar(
            image = painterResource(id = R.drawable.petsss),
            height = (LocalConfiguration.current.screenWidthDp * 0.2f).dp,
            borderCircle = 2.dp
        )
        Spacer(modifier = Modifier.width(5.dp))
        val imagePainter = painterResource(id = R.drawable.unionbg) // Replace with your image URL

        InstructUpsImageBackground(
            imagePainter = imagePainter,
            modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp * 0.7f)
                .height(80.dp),
            contentScale = ContentScale.FillBounds
        ) {
            Column(
                modifier = Modifier.padding(start = 15.dp, end = 11.dp, bottom = 11.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = textInstruct,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 9.sp),
                    color = Color(0xff4553B7)
                )
                Text(
                    text = contentInstruct,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 14.sp,
                        lineHeight = 17.sp,

                        ),
                    maxLines = 2,
                    color = Color(0xff111245)
                )
            }
        }

    }
}

@Composable
fun CircleAvatar(
    modifier: Modifier = Modifier,
    image: Painter,
    height: Dp,
    isExitsImage: Boolean = false,
    urlImage: String? = null,
    borderCircle: Dp,
) {
    val sizeImage = height * 0.9f
    Box(
        modifier = modifier
            .size(height)
            .border(
                border = BorderStroke(borderCircle, Color(0xffCFD0E4)),
                shape = CircleShape
            )
            .background(Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (!isExitsImage) {
            Image(
                modifier = modifier.size(sizeImage),
                painter = image,
                contentDescription = "Image Camera"
            )
        } else {
            // Nếu có ảnh từ data base thì hiển thị ở đây
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(urlImage)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(sizeImage)
            )
        }
    }

}

@Composable
fun InstructUpsImageBackground(
    imagePainter: Painter,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillBounds,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = imagePainter,
            contentDescription = null,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            content()
        }
    }
}