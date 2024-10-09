package com.example.quizizz_app.activities

import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quizizz_app.R
import com.example.quizizz_app.activities.components.CustomBigButton
import com.example.quizizz_app.activities.components.CustomOutlinedTextField
import com.example.quizizz_app.activities.components.CustomOutlinedTextFieldPassword
import com.example.quizizz_app.activities.components.CustomTextFontSize
import com.example.quizizz_app.activities.components.CustomTextFontSizeMedium
import com.example.quizizz_app.activities.components.CustomTextOnClick
import com.example.quizizz_app.utils.saveLoginState
import com.example.quizizz_app.viewModels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {

    var loginName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginNameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val loginSuccess by loginViewModel.loginSuccess.observeAsState()
    val errorMessage by loginViewModel.errorMessage.observeAsState()
    val emailError by loginViewModel.emailError.observeAsState()
    val passwordError by loginViewModel.passwordError.observeAsState()

    LaunchedEffect(loginSuccess, errorMessage) {
        if (loginSuccess == true) {
            Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
            navController.navigate("HomeScreen"){
                popUpTo("LoginScreen") { inclusive = true }
            }
        } else {
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                loginViewModel.resetErrorMessage()  // Reset lỗi sau khi hiển thị
            }
        }
    }

    // Focus vào trường có lỗi
    LaunchedEffect(emailError, passwordError) {
        if (emailError == true) {
            loginNameFocusRequester.requestFocus()
        } else if (passwordError == true) {
            passwordFocusRequester.requestFocus()
        }
    }

    Column(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
            .fillMaxSize()
            .background(Color.White)
            .imePadding(),
        verticalArrangement = Arrangement.Center
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
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Spacer(modifier = Modifier.padding(15.dp))
            CustomOutlinedTextField(
                value = loginName,
                onValueChange = { loginName = it },
                label = "Email đăng nhập",
                modifier = Modifier.focusRequester(loginNameFocusRequester)
            )
            Spacer(modifier = Modifier.padding(5.dp))
            CustomOutlinedTextFieldPassword(
                value = password,
                onValueChange = { password = it },
                label = "Mật khẩu",
                modifier = Modifier.focusRequester(passwordFocusRequester)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = "Quên mât khẩu ?",
                fontSize = 16.sp,
                color = Color(0xFF2880c6),
                fontWeight = FontWeight(600),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            navController.navigate("ForgotPasswordScreen")
                        })
                    }
                    .align(Alignment.End)
            )
            Spacer(modifier = Modifier.padding(10.dp))

            CustomBigButton(
                title = "Đăng nhập",
                onClick = {
                    loginViewModel.login(context, loginName, password)
                }
            )

            Spacer(modifier = Modifier.padding(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Spacer(modifier = Modifier.width(10.dp))
                CustomTextFontSize(
                    title = "Bạn chưa có tài khoản ?",
                    color = Color(0xFF71727b),
                    fontSize = 16
                )
                Spacer(modifier = Modifier.width(10.dp))
                CustomTextOnClick(
                    title = "Đăng ký",
                    color = Color(0xFF0971e9),
                    fontSize = 16,
                    onclick = { navController.navigate("SignUpScreen") })
            }
        }
    }

}