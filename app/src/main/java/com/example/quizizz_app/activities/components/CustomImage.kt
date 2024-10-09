package com.example.quizizz_app.activities.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
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
    // Tạo một Map để lưu trạng thái tải cho từng hình ảnh dựa trên URL
    var imageLoadingState by remember { mutableStateOf(mutableMapOf<String, Boolean>()) }

    // Kiểm tra xem trạng thái tải của hình ảnh hiện tại dựa trên URL
    val isImageLoading = imageLoadingState[imageUrl] ?: true

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
                        onSuccess = { _, _ ->
                            // Cập nhật trạng thái khi hình ảnh được tải thành công
                            imageLoadingState = imageLoadingState.toMutableMap().apply {
                                put(imageUrl, false)
                            }
                        },
                        onError = { _, _ ->
                            // Cập nhật trạng thái khi xảy ra lỗi tải hình ảnh
                            imageLoadingState = imageLoadingState.toMutableMap().apply {
                                put(imageUrl, false)
                            }
                        }
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

        // Hiển thị CircularProgressIndicator nếu ảnh đang được tải
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

@Composable
fun ImageRank(
    title: String ?= "",
    point: String ?= "",
    imgUrl: String? = "",
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xffccd8e8)
){

    val nameParts = title?.split(" ")
    val lastName = nameParts?.last()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = lastName!!,  // Hiển thị tên cuối cùng
            modifier = Modifier
                .widthIn(max = 80.dp),  // Giới hạn chiều rộng
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            ),
            color = Color.White,
            maxLines = 1,  // Chỉ hiển thị một dòng
            overflow = TextOverflow.Ellipsis  // Hiển thị "..." nếu vượt quá độ dài
        )
        Spacer(modifier = Modifier.padding(top = 2.dp))
        Text(
            text = point!!,
            modifier = Modifier,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            ),
            color = Color.White
        )
        Spacer(modifier = Modifier.padding(top = 5.dp))
        Box(
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(shape = CircleShape)
                    .background(backgroundColor)
            )

            Box(
                modifier = Modifier
                    .clip(shape = CircleShape)
            ){
                Box(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                ) {
                    if (imgUrl != "") {
                        AsyncImage(
                            model = imgUrl,
                            contentDescription = "Rank 1",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(70.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painterResource(id = R.drawable.petsss),
                            contentDescription = "Default Image",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(70.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

            }
        }
    }
}
