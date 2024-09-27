package com.example.quizizz_app.activities

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.* // Import Layout
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.* // Import Material3 components
import androidx.compose.runtime.* // Import Composable functions
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quizizz_app.R
import com.example.quizizz_app.activities.components.ButtonItem
import com.example.quizizz_app.activities.components.EgTopAppBar
import com.example.quizizz_app.activities.components.DrawerContent // Import DrawerContent
import com.example.quizizz_app.utils.checkAndSaveTrialExpired
import com.example.quizizz_app.utils.isLoggedIn
import com.example.quizizz_app.utils.saveLoginState
import com.example.quizizz_app.utils.saveTrialStartTime
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
) {
    val context = LocalContext.current

    // Kiểm tra và lưu trạng thái thử nghiệm
    val trialExpired = remember { mutableStateOf(false) }

    // Kiểm tra xem người dùng đã đăng nhập hay chưa
    val loggedIn = isLoggedIn(context)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController)
        },
        scrimColor = Color.Transparent
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                EgTopAppBar(
                    titleRes = "Quizizz",
                    navigationIcon = ImageVector.vectorResource(id = R.drawable.icon_left_bar),
                    navigationIconContentDescription = "Navigation icon",
                    actionIcon = Icons.Default.Person,
                    actionIconContentDescription = "Action icon",
                    onNavigationClick = {
                        scope.launch {
                            drawerState.open() // Mở Drawer
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(10.dp)
            ) {
                ButtonItem(
                    titleButton = "Bắt đầu",
                    textColor = Color.White,
                    icon = painterResource(id = R.drawable.learning),
                    gradient = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xffD54646),
                            Color(0xffFF9064)
                        )
                    ),
                    shadowColor = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xff9E3332),
                            Color(0xff9E3332)
                        )
                    ),
                    shadowBottomOffset = 8f,
                    onClick = {
                        val sharedPreferences = context.getSharedPreferences("TrialPrefs", Context.MODE_PRIVATE)
                        val trialStartTime = sharedPreferences.getLong("trialStartTime", 0L)

                        // Nếu chưa có thời gian dùng thử, lưu lại thời gian hiện tại
                        if (trialStartTime == 0L) {
                            saveTrialStartTime(context)
                        }
                        // Cập nhật trạng thái trialExpired trước khi cho phép điều hướng
                        trialExpired.value = checkAndSaveTrialExpired(context)

                        if (loggedIn) {
                            // Nếu người dùng đã đăng nhập, cho phép điều hướng đến QuizScreen
                            navController.navigate("QuizScreen") {
                                popUpTo("WelcomeScreen") { inclusive = true }
                            }
                        } else {
                            // Nếu thời gian thử nghiệm đã hết, yêu cầu đăng nhập
                            if (trialExpired.value) {
                                saveLoginState(context, false)
                                Toast.makeText(context, "Thời gian dùng thử đã hết, vui lòng đăng nhập để tiếp tục!", Toast.LENGTH_SHORT).show()
                                navController.navigate("LoginScreen") {
                                    popUpTo("HomeScreen") { inclusive = true }
                                }
                            } else {
                                // Nếu thời gian thử nghiệm chưa hết, cho phép chơi
                                navController.navigate("QuizScreen") {
                                    popUpTo("WelcomeScreen") { inclusive = true }
                                }
                            }
                        }
                    }

                )
            }
        }
    }
}

