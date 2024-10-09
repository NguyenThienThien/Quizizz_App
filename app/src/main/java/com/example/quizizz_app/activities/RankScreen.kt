package com.example.quizizz_app.activities

import AnimatedCounterRank
import ItemRankUser
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quizizz_app.R
import com.example.quizizz_app.activities.components.ImageRank
import com.example.quizizz_app.activities.components.TopAppBarItem
import com.example.quizizz_app.utils.getUserId
import com.example.quizizz_app.viewModels.RankViewModel
import com.example.quizizz_app.viewModels.ResultStudyViewModel

@Composable
fun RankScreen(
    navController: NavController,
    resultStudyViewModel: ResultStudyViewModel = viewModel(),
    rankViewModel: RankViewModel = viewModel()
) {

    val configuration = LocalConfiguration.current // Lấy cấu hình màn hình hiện tại
    val screenWidth = configuration.screenWidthDp.dp // Chiều rộng màn hình tính theo dp
    val screenHeight = configuration.screenHeightDp.dp // Chiều cao màn hình tính theo dp

    val typeRanks = listOf("Tổng", "Ngày", "Tuần", "Tháng", "Nửa năm", "1 năm")
    val chooseRank = listOf("Điểm cao nhất", "Chuỗi trả lời", "Tổng câu đúng")
    var selectedTypeRank by remember { mutableStateOf(typeRanks[0]) }  // Trạng thái của lựa chọn thứ nhất
    var selectedChooseRank by remember { mutableStateOf(chooseRank[0]) }  // Trạng thái của lựa chọn thứ hai

    val userNames by rankViewModel.userNameMap.observeAsState(emptyMap())

    val context = LocalContext.current
    val userId = getUserId(context)
    if (userId != null) {
        resultStudyViewModel.setUserId(userId)
    } else {
        Log.e("DrawerContent", "User không tồn tại!")
    }

    // Trigger data fetching when selections change
    LaunchedEffect(selectedTypeRank, selectedChooseRank) {
        rankViewModel.fetchRankedUsers(selectedTypeRank, selectedChooseRank)
    }

    val rankedUsers by rankViewModel.rankedUsers.observeAsState(emptyList())
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .background(Color(0xff333DAD))
            .paint(
                painterResource(id = R.drawable.shine),
                contentScale = ContentScale.Crop
            )
    ) {
        // Phần trên chiếm 6 phần
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(5.5f)
        ) {
            TopAppBarItem(
                modifier = Modifier
                    .background(Color.Transparent),
                iconLeft = painterResource(id = R.drawable.button_back),
                contentLogo = { modifier ->
                    Text(
                        text = "Xếp hạng",
                        modifier = modifier,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = Color(0xff111245)
                    )
                },
                iconRight = { modifier ->
                    Icon(
                        Icons.Filled.Menu,
                        contentDescription = "",
                        modifier = modifier,
                        tint = Color(0xff353a75),
                    )
                },
                backgroundColor = Brush.linearGradient(listOf(Color.White, Color.White)),
                onClickLeftIcon = { navController.popBackStack() },
                onClickRightIcon = {},
                onClick = {}
            )

            Column(
                modifier = Modifier
                    .padding(top = if (screenWidth < 360.dp) 80.dp else 50.dp) // Tạo khoảng cách với TopAppBar
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(), // Đảm bảo Row chiếm toàn bộ chiều rộng
                    horizontalArrangement = Arrangement.SpaceBetween // Cân đều các phần tử trong Row
                ) {
                    // Sử dụng weight để chia đều không gian giữa các AnimatedCounterRank
                    AnimatedCounterRank(
                        titleList = typeRanks,
                        modifier = Modifier.weight(1f),
                        onItemSelected = { item ->
                            selectedTypeRank = item
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp)) // Khoảng cách giữa các phần tử
                    AnimatedCounterRank(
                        titleList = chooseRank,
                        modifier = Modifier.weight(1f),
                        onItemSelected = { item ->
                            selectedChooseRank = item
                        }
                    )
                }
            }

            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center, // Cân đều không gian giữa các cột
                    verticalAlignment = Alignment.CenterVertically // Căn chỉnh các phần tử theo chiều dọc
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(35.dp)
                    ) {
                        if (rankedUsers.size >= 2) {
                            ImageRank(
                                title = userNames[rankedUsers[1].userId] ?: "Unknown", // Get name from map
                                point = when (selectedChooseRank) {
                                    "Chuỗi trả lời" -> rankedUsers[1].highestStreak.toString() // Hiển thị Chuỗi trả lời
                                    "Tổng câu đúng" -> rankedUsers[1].totalCorrectAnswers.toString() // Hiển thị Tổng câu đúng
                                    else -> rankedUsers[1].score.toString() // Hiển thị Điểm cao nhất
                                },
                                imgUrl = rankedUsers[1].imageUrl,
                                modifier = Modifier.padding(top = 20.dp)
                            )
                        }else {
                            Spacer(modifier = Modifier.size(100.dp)) // Giữ không gian
                        }

                        if (rankedUsers.isNotEmpty()) {
                            ImageRank(
                                title = userNames[rankedUsers[0].userId] ?: "Unknown", // Get name from map
                                point = when (selectedChooseRank) {
                                    "Chuỗi trả lời" -> rankedUsers[0].highestStreak.toString() // Hiển thị Chuỗi trả lời
                                    "Tổng câu đúng" -> rankedUsers[0].totalCorrectAnswers.toString() // Hiển thị Tổng câu đúng
                                    else -> rankedUsers[0].score.toString() // Hiển thị Điểm cao nhất
                                },
                                imgUrl = rankedUsers[0].imageUrl,
                                modifier = Modifier.padding(top = 10.dp),
                                backgroundColor = Color(0xFFF66D49)
                            )
                        }else {
                            Spacer(modifier = Modifier.size(100.dp)) // Giữ không gian
                        }

                        if (rankedUsers.size >= 3) {
                            ImageRank(
                                title = userNames[rankedUsers[2].userId] ?: "Unknown", // Get name from map
                                point = when (selectedChooseRank) {
                                    "Chuỗi trả lời" -> rankedUsers[2].highestStreak.toString() // Hiển thị Chuỗi trả lời
                                    "Tổng câu đúng" -> rankedUsers[2].totalCorrectAnswers.toString() // Hiển thị Tổng câu đúng
                                    else -> rankedUsers[2].score.toString() // Hiển thị Điểm cao nhất
                                },
                                imgUrl = rankedUsers[2].imageUrl,
                                modifier = Modifier.padding(top = 20.dp)
                            )
                        }else {
                            Spacer(modifier = Modifier.size(80.dp)) // Giữ không gian
                        }
                    }
                }


                Image(
                    painter = painterResource(id = R.drawable.group),
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }

        // Phần dưới chiếm 4 phần
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
        ) {
            LazyColumn(state = listState) {
                itemsIndexed(rankedUsers) { index, user ->
                    if (userId != null) {
                        ItemRankUser(
                            index = index,
                            quizResult = user,
                            selectedChooseRank = selectedChooseRank,
                            currentUserId = userId
                        )
                    }
                    Divider()
                }
            }
        }

        LaunchedEffect(rankedUsers) {
            val userIndex = rankedUsers.indexOfFirst { it.userId == userId }
            if (userIndex != -1) {
                listState.scrollToItem(userIndex)
            }
        }
    }
}