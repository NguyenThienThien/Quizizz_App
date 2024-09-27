package com.example.quizizz_app.activities.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.quizizz_app.R

@Composable
fun DialogConfirmExit(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = Color(0xff78799C), RoundedCornerShape(25.dp))
        ) {
            Box(
                modifier = modifier
                    .padding(top = 2.dp, start = 2.dp, end = 2.dp, bottom = 5.dp)
                    .background(
                        color = Color.White, shape = RoundedCornerShape(25.dp)
                    )
            ) {
                Box(
                    modifier = modifier
                        .size(50.dp)
                        .align(alignment = Alignment.TopEnd)
                        .offset(x = 20.dp, y = (-20).dp)
                ) {
                    HeaderImage(modifier = modifier.fillMaxSize())
                }

                Column(
                    modifier.padding(bottom = 12.dp)
                ) {
                    Column(
                        modifier = modifier.padding(top = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "Thoát",
                            modifier = modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.displayMedium.copy(
                                color = Color(0xff111245),
                                textAlign = TextAlign.Center
                            ),
                        )
                        Text(
                            text = "Bạn có chắc chắn muốn thoát? Bạn sẽ phải chơi lại từ đầu khi quay lại.",
                            modifier = modifier
                                .wrapContentWidth()
                                .padding(start = 14.dp, end = 14.dp),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color(0xff111245),
                            ),
                        )
                        Row(
                            modifier = modifier
                                .padding(start = 14.dp, end = 14.dp, top = 50.dp)
                                .fillMaxWidth()
                        ) {
                            BottomItem(
                                modifier = modifier
                                    .height(54.dp)
                                    .weight(1f),
                                onClick = onClick,
                                name = "Có",
                                textColor = Color.White,
                                gradientColor = Brush.linearGradient(listOf(Color(0xffD54646), Color(0xffFF9064)))
                            )

                            Spacer(modifier = modifier.width(8.dp))
                            BottomItem(
                                modifier = modifier
                                    .height(54.dp)
                                    .weight(1f),
                                onClick = onDismiss,
                                name = "Không",
                                textColor = Color(0xffF2564D),
                                gradientColor = Brush.linearGradient(listOf(Color.White, Color.White))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_exit),
        contentDescription = "Back",
        modifier = modifier
    )
}

@Composable
@Preview
fun PreviewDialogConfirmExit() {
    DialogConfirmExit(
        onDismiss = {},
        onClick = {}
    )
}