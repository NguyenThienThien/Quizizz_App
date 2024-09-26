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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quizizz_app.R
import com.example.quizizz_app.activities.components.CustomBigButton
import com.example.quizizz_app.activities.components.CustomOutlinedTextField
import com.example.quizizz_app.activities.components.CustomOutlinedTextFieldPassword
import com.example.quizizz_app.activities.components.CustomTextFontSizeMedium
import com.example.quizizz_app.utils.saveLoginState
import com.example.quizizz_app.viewModels.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun SignUpScreen(
    navController: NavController,
    signUpViewModel: SignUpViewModel = viewModel()
) {

    var emailUser by remember { mutableStateOf("") }
    var passWordUser by remember { mutableStateOf("") }
    var rePassWordUser by remember { mutableStateOf("") }

    // Tạo FocusRequester cho từng TextField
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val rePasswordFocusRequester = remember { FocusRequester() }

    val signUpSuccess by signUpViewModel.signUpSuccess.observeAsState()
    val errorMessage by signUpViewModel.errorMessage.observeAsState()
    val emailError by signUpViewModel.emailError.observeAsState()
    val passwordError by signUpViewModel.passwordError.observeAsState()
    val rePasswordError by signUpViewModel.rePasswordError.observeAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(signUpSuccess, errorMessage) {
        if (signUpSuccess == true) {
            Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
            navController.navigate("LoginScreen")
        } else {
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                signUpViewModel.resetErrorMessage()  // Reset lỗi sau khi hiển thị
            }
        }
    }

    LaunchedEffect(emailError, passwordError, rePasswordError) {
        if (emailError == true) {
            emailFocusRequester.requestFocus()
        } else if (passwordError == true) {
            passwordFocusRequester.requestFocus()
        }else if (rePasswordError == true){
            rePasswordFocusRequester.requestFocus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
            .statusBarsPadding()
            .imePadding()
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
                .fillMaxHeight()
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
            modifier = Modifier
                .background(Color.White)
                .padding(20.dp)
        ) {

            Spacer(modifier = Modifier.padding(15.dp))
            CustomOutlinedTextField(
                value = emailUser,
                onValueChange = { emailUser = it },
                label = "Nhập email",
                modifier = Modifier
                    .focusRequester(emailFocusRequester)
            )

            Spacer(modifier = Modifier.padding(10.dp))
            CustomOutlinedTextFieldPassword(
                value = passWordUser,
                onValueChange = { passWordUser = it },
                label = "Mật khẩu",
                modifier = Modifier
                    .focusRequester(passwordFocusRequester)
            )

            Spacer(modifier = Modifier.padding(10.dp))
            CustomOutlinedTextFieldPassword(
                value = rePassWordUser,
                onValueChange = { rePassWordUser = it },
                label = " Nhập lại Mật khẩu",
                modifier = Modifier
                    .focusRequester(rePasswordFocusRequester)
            )

            Spacer(modifier = Modifier.padding(20.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bằng việc nhấn nút Tiếp tục đã đồng ý với",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)
                )

                Text(
                    text = "Điều khoản và chính sách sử dụng",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff005595)
                )
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            CustomBigButton(
                title = "Tiếp tục",
                onClick = {
                    signUpViewModel.signUp(context, emailUser, passWordUser, rePassWordUser)
                }
            )
        }
    }

}