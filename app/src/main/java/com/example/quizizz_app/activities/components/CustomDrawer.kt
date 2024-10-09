package com.example.quizizz_app.activities.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.quizizz_app.R
import com.example.quizizz_app.utils.getUserId
import com.example.quizizz_app.utils.saveLoginState
import com.example.quizizz_app.viewModels.ResultStudyViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope

@Composable
fun DrawerContent(
    navController: NavController, // Truyền NavController vào DrawerContent
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
    val imageUrlOld by resultStudyViewModel.imageUrl.observeAsState("")
    val userNameOld by resultStudyViewModel.userName.observeAsState("")
    val emailOld by resultStudyViewModel.email.observeAsState("")
    val gradientButtonLogin = Brush.verticalGradient(
        colors = listOf(
            Color(0xff7F77FE),
            Color(0xffA573FF)
        )
    )

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
            .fillMaxHeight() // Sử dụng fillMaxSize để chiếm toàn bộ màn hình
            .fillMaxWidth(0.85f)
            .background(brush = gradientButtonLogin) // Áp dụng gradient cho backgroun
    ) {
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (imageUrlOld.isNotEmpty()) {
                Image(
                    painter = rememberImagePainter(imageUrlOld),
                    contentDescription = "",
                    modifier = Modifier.size(90.dp).clip(RoundedCornerShape(50.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Sử dụng hình ảnh mặc định nếu không có URL
                Image(
                    painter = painterResource(R.drawable.petsss),
                    contentDescription = "",
                    modifier = Modifier.size(90.dp),
                )
            }

            Column(modifier = Modifier.width(153.dp)) {
                Text(
                    text = userNameOld,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.White,
                    maxLines = 1, // Giới hạn số dòng là 1
                    overflow = TextOverflow.Ellipsis // Hiển thị dấu ba chấm khi quá dài
                )
                Text(
                    text = emailOld,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(500),
                    ),
                    color = Color(0xffD2D2D2),
                    maxLines = 1, // Giới hạn số dòng là 1
                    overflow = TextOverflow.Ellipsis // Hiển thị dấu ba chấm khi quá dài
                )
            }


            IconButton(
                onClick = {
                    navController.navigate("ChangeInformationScreen")
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.pen),
                    contentDescription = "Logout icon",
                    modifier = Modifier.size(25.dp)
                )
            }

        }
        Spacer(modifier = Modifier.padding(3.dp))
        NavigationItem(
            text = "Giải Quiz",
            image = R.drawable.playhaha,
            onClick = {
                navController.navigate("QuizScreen") {
                    popUpTo("WelcomeScreen") { inclusive = true }
                }
            },
            navigationController = navController
        )
        NavigationItem(
            text = "Xếp hạng",
            image = R.drawable.rank,
            onClick = {
                navController.navigate("RankScreen") {
                    popUpTo("WelcomeScreen") { inclusive = true }
                }
            },
            navigationController = navController
        )
        NavigationItem(
            text = "Đăng xuất",
            image = R.drawable.logout,
            onClick = {
                FirebaseAuth.getInstance().signOut()
                saveLoginState(context, false, null)
                navController.navigate("LoginScreen")
            },
            navigationController = navController
        )
    }
}


@Composable
fun NavigationItem(
    text: String,
    image: Int,
    onClick: () -> Unit,
    navigationController: NavController,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .clickable{ onClick() }
            .padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "",
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.padding(start = 20.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = Color.White
        )
    }
    Spacer(modifier = Modifier.padding(8.dp))
}
