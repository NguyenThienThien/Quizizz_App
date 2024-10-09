package com.example.quizizz_app.activities

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quizizz_app.R
import com.example.quizizz_app.activities.components.CustomBigButton
import com.example.quizizz_app.activities.components.CustomOutlinedTextField
import com.example.quizizz_app.activities.components.CustomOutlinedTextFieldPassword
import com.example.quizizz_app.activities.components.CustomTextFontSize
import com.example.quizizz_app.activities.components.CustomTextFontSizeMedium
import com.example.quizizz_app.activities.components.CustomTextOnClick
import com.example.quizizz_app.viewModels.ForgotPasswordViewModel

@Composable
fun ForgotPassWordScreen(
    navController: NavController,
    forgotPasswordViewModel: ForgotPasswordViewModel = viewModel()
) {

    var emailUser by remember { mutableStateOf("") }
    val emailFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val resetSuccess by forgotPasswordViewModel.resetSuccess.observeAsState(false)
    val errorMessage by forgotPasswordViewModel.errorMessage.observeAsState()


    LaunchedEffect(resetSuccess, errorMessage) {
        if (resetSuccess) {
            Toast.makeText(context, "Đã gửi email khôi phục mật khẩu!", Toast.LENGTH_SHORT).show()
            navController.navigate("LoginScreen"){
                popUpTo("ForgotPasswordScreen") { inclusive = true }
            }
        } else {
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                forgotPasswordViewModel.resetErrorMessage()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Center,
    ) {
        IconButton(
            onClick = { navController.popBackStack() }
        ) {
            Icon(
                Icons.Default.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .clickable { navController.popBackStack() },
                tint = Color(0xff9fa5aa)
            )
        }
        Column(
            modifier = Modifier
                .padding(10.dp),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.petsss),
                    contentDescription = null,
                    modifier = Modifier
                        .size(90.dp)
                        .padding(10.dp)
                )
                Spacer(modifier = Modifier.padding(7.dp))
                Box(
                    modifier = Modifier
                        .height(35.dp)
                        .width(140.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFe7f2fa)),
                    contentAlignment = Alignment.Center
                ) {
                    CustomTextFontSizeMedium(title = "Quizizz", color = Color(0xFF2880c6))
                }
            }
        }

        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Spacer(modifier = Modifier.padding(15.dp))
            CustomOutlinedTextField(
                value = emailUser,
                onValueChange = { emailUser = it },
                label = "email lấy lại mật khẩu !",
                modifier = Modifier.focusRequester(emailFocusRequester)
            )

            Spacer(modifier = Modifier.padding(10.dp))

            CustomBigButton(
                title = "Gửi email",
                onClick = {
                    forgotPasswordViewModel.resetPassword(emailUser)
                }
            )
        }
    }
}