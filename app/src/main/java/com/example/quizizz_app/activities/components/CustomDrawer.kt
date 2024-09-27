package com.example.quizizz_app.activities.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizizz_app.R
import com.example.quizizz_app.utils.saveLoginState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope

@Composable
fun DrawerContent(
    navController: NavController // Truyền NavController vào DrawerContent
) {
    val gradientButtonLogin = Brush.horizontalGradient(
        colors = listOf(
            Color(0xffD54646),
            Color(0xffFF9064)
        )
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
            .fillMaxHeight() // Sử dụng fillMaxSize để chiếm toàn bộ màn hình
            .fillMaxWidth(0.85f)
            .background(brush = gradientButtonLogin) // Áp dụng gradient cho background
            .statusBarsPadding()
    ) {
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.petsss),
                contentDescription = "",
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.padding(10.dp))

            Text(
                text = "User 01",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                ),
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        NavigationDrawerItem(
            modifier = Modifier.fillMaxWidth(), // Đảm bảo mục này chiếm toàn bộ chiều rộng
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color.Transparent,
                unselectedContainerColor = Color.Transparent
            ),
            label = {
                Text(
                    text = "Đăng xuất",
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                    color = Color.Black
                )
            },
            icon = {
                Image(
                    painter = painterResource(R.drawable.logout),
                    contentDescription = "Logout icon",
                    modifier = Modifier.size(25.dp)
                )
            },
            selected = false,
            onClick = {
                FirebaseAuth.getInstance().signOut()
                saveLoginState(navController.context, false)
                navController.navigate("LoginScreen")
            }
        )
        Divider() // Dòng kẻ ngăn cách giữa các mục
    }
}


@Composable
fun NavigationItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    navigationController: NavController,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    additionalText: String? = null // Optional phone number
) {
    NavigationDrawerItem(
        modifier = Modifier.background(Color.Transparent),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color.Transparent,
            unselectedContainerColor = Color.Transparent,
        ),
        label = {
            Row {
                Text(
                    text = text,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                    color = Color.Black
                )
                if (additionalText != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = additionalText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        color = Color.Blue
                    )
                }
            }
        },
        icon = { Icon(icon, contentDescription = null, modifier = Modifier.size(10.dp)) },
        selected = false,
        onClick = {
            onClick()
        }
    )
    Spacer(modifier = Modifier.padding(5.dp))
}
