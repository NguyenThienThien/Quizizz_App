package com.example.quizizz_app.activities.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quizizz_app.R

@Composable
fun ImageAudio(
    modifier: Modifier = Modifier,
    imageUrl: String,
    height: Dp,
    width: Dp,
    isPlay: Boolean = false
) {
    var isImageLoading by remember { mutableStateOf(true) }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .size(height = height, width = width)
    ) {
        ConstraintLayout(modifier = modifier.matchParentSize()) {
            val (image, icon) = createRefs()
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .listener(
                        onSuccess = { _, _ -> isImageLoading = false },
                        onError = { _, _ -> isImageLoading = false }
                    )
                    .build(),
                modifier = modifier
                    .padding(bottom = 22.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                contentDescription = "Image Audio",
                contentScale = ContentScale.Crop
            )
            val painter =
                if (isPlay) painterResource(id = R.drawable.pause) else painterResource(id = R.drawable.play)
            Image(
                painter = painter, contentDescription = "Icon Play",
                modifier = modifier.constrainAs(icon) {
                    end.linkTo(parent.end, margin = 12.dp)
                    bottom.linkTo(parent.bottom)
                }
            )
        }

        if (isImageLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent, RoundedCornerShape(10.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xff4169E1))
            }
        }
    }
}