package com.example.quizizz_app.activities

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quizizz_app.R
import com.example.quizizz_app.activities.components.AnimatedCounter
import com.example.quizizz_app.activities.components.AnimatedCounter2
import com.example.quizizz_app.activities.components.ButtonItem
import com.example.quizizz_app.utils.getUserId
import com.example.quizizz_app.viewModels.ResultStudyViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ResultStudyScreen(
    navController: NavController,
    score: Int,
    highestStreak: Int,
    totalCorrectAnswers: Int,
    modifier: Modifier = Modifier,
    resultStudyViewModel: ResultStudyViewModel = viewModel()
) {
    val context = LocalContext.current
    val userId = getUserId(context)
    if (userId != null) {
        resultStudyViewModel.setUserId(userId)
    } else {
        Log.e("DrawerContent", "User không tồn tại!")
    }

    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xff333DAD)
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xff333DAD))
            .padding(10.dp)
            .paint(
                painterResource(id = R.drawable.shine),
                contentScale = ContentScale.Crop
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val widthScreen = LocalConfiguration.current.screenWidthDp
        val widthFullItem = (widthScreen * 0.45f).dp
        Spacer(modifier = modifier.height(24.dp))
        Column(
            modifier = modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Hoàn thành danh sách câu hỏi!",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.W800,
                    textAlign = TextAlign.Center
                ),
                color = Color.White
            )
            Text(
                text = "Bạn đạt được: ",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            Spacer(modifier = modifier.height(12.dp))
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    AnimatedCounter2(
                        point = score,
                        imageResId = R.drawable.image14,
                        title = "score",
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    AnimatedCounter2(
                        point = highestStreak,
                        imageResId = R.drawable.image15,
                        title = "Streak"
                    )
                }
            }
            Spacer(modifier = modifier.height(12.dp))
        }

        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonItem(
                nameButton = "Đóng",
                textColor = Color.White,
                gradient = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xffD54646),
                        Color(0xffFF9064)
                    )
                ),
                shadowColor = Color(0xff9E3332),
                paddingButton = 16.dp,
                shadowBottomOffset = 6f,
                onClick = {
                    if(userId != null){
                        resultStudyViewModel.updateScoreAndStreak(score, highestStreak, totalCorrectAnswers, userId)
                        navController.navigate("HomeScreen")
                    } else {
                        navController.navigate("HomeScreen")
                    }
                }
            )
            Spacer(modifier = modifier.height(24.dp))
        }

    }
}