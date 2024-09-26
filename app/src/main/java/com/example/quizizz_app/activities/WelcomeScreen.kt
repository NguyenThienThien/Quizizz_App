package com.example.quizizz_app.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quizizz_app.R
import com.example.quizizz_app.utils.isLoggedIn
import com.example.quizizz_app.viewModels.QuizViewModel
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    navController: NavController,
    quizViewModel: QuizViewModel = viewModel()
) {

    val context = LocalContext.current
    val isLoading by quizViewModel.isLoading.observeAsState(initial = true)

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            val nextScreen = if (isLoggedIn(context)) {
                "HomeScreen" // Chuyển thẳng đến HomeScreen nếu đã đăng nhập
            } else {
                "LoginScreen" // Nếu chưa đăng nhập thì vào LoginScreen để đăng nhập
            }
            navController.navigate(nextScreen) {
                popUpTo("WelcomeScreen") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.petsss),
            contentDescription = "",
            modifier = Modifier.size(90.dp)
        )
    }
}

@Composable
@Preview
fun WelcomeScreenPreview() {
    WelcomeScreen(navController = rememberNavController())
}
