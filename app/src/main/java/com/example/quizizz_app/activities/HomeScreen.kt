package com.example.quizizz_app.activities

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.* // Import Layout
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.* // Import Material3 components
import androidx.compose.runtime.* // Import Composable functions
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quizizz_app.R
import com.example.quizizz_app.activities.components.AnimatedCounter2
import com.example.quizizz_app.activities.components.ButtonItem
import com.example.quizizz_app.activities.components.EgTopAppBar
import com.example.quizizz_app.activities.components.DrawerContent // Import DrawerContent
import com.example.quizizz_app.activities.components.SeeResult
import com.example.quizizz_app.utils.checkAndSaveTrialExpired
import com.example.quizizz_app.utils.getUserId
import com.example.quizizz_app.utils.isLoggedIn
import com.example.quizizz_app.utils.saveLoginState
import com.example.quizizz_app.utils.saveTrialStartTime
import com.example.quizizz_app.viewModels.ResultStudyViewModel
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    resultStudyViewModel: ResultStudyViewModel = viewModel()
) {
    val context = LocalContext.current

    val configuration = LocalConfiguration.current // Lấy cấu hình màn hình hiện tại
    val screenWidth = configuration.screenWidthDp.dp // Chiều rộng màn hình tính theo dp
    val screenHeight = configuration.screenHeightDp.dp // Chiều cao màn hình tính theo dp

    val trialExpired = remember { mutableStateOf(false) }
    val loggedIn = isLoggedIn(context)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val userId = getUserId(context)
    if (userId != null) {
        resultStudyViewModel.setUserId(userId)
    } else {
        Log.e("DrawerContent", "User không tồn tại!")
    }
    val score by resultStudyViewModel.score.observeAsState(0)
    val highestStreak by resultStudyViewModel.scoreStreak.observeAsState(0)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController)
        },
        scrimColor = Color.Transparent
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
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
                    },
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    painter = painterResource(R.drawable.banner),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (screenWidth < 400.dp) 150.dp else 200.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    SeeResult(
                        point = score.toString(),
                        imageResId = R.drawable.image14,
                        title = "Điểm cao nhất bạn từng đạt được !",
                        modifier = Modifier.weight(1f) // Chiếm một nửa chiều rộng
                    )
                    Spacer(modifier = Modifier.width(10.dp)) // Khoảng cách giữa các item
                    SeeResult(
                        point = highestStreak.toString(),
                        imageResId = R.drawable.image15,
                        title = "Chuỗi cao nhất bạn từng đạt được !",
                        modifier = Modifier.weight(1f) // Chiếm một nửa chiều rộng
                    )
                }

                Spacer(modifier = Modifier.padding(10.dp))
                ButtonItem(
                    titleButton = "Bắt đầu",
                    textColor = Color.White,
                    icon = painterResource(id = R.drawable.learning),
                    gradient = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xff7F77FE),
                            Color(0xffA573FF)
                        )
                    ),
                    shadowColor = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xff7F77FE),
                            Color(0xffA573FF)
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
                                saveLoginState(context, false, null)
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

                Spacer(modifier = Modifier.padding(10.dp))

                ButtonItem(
                    titleButton = "Xếp hạng",
                    textColor = Color.White,
                    icon = painterResource(id = R.drawable.icon_rank_bk),
                    gradient = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xff7F77FE),
                            Color(0xffA573FF)
                        )
                    ),
                    shadowColor = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xff7F77FE),
                            Color(0xffA573FF)
                        )
                    ),
                    shadowBottomOffset = 8f,
                    onClick = {
                        navController.navigate("RankScreen") {
                            popUpTo("WelcomeScreen") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

