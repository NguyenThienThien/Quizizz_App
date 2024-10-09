package com.example.quizizz_app.activities

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.quizizz_app.R
import com.example.quizizz_app.activities.components.CustomBigButton
import com.example.quizizz_app.activities.components.CustomOutlinedTextField
import com.example.quizizz_app.activities.components.CustomOutlinedTextFieldPassword
import com.example.quizizz_app.activities.components.CustomTextFontSizeMedium
import com.example.quizizz_app.models.User
import com.example.quizizz_app.utils.getUserId
import com.example.quizizz_app.viewModels.ChangeInformationViewModel
import com.example.quizizz_app.viewModels.ResultStudyViewModel

@Composable
fun ChangeInformation(
    navController: NavController,
    user: User? = null,
    resultStudyViewModel: ResultStudyViewModel = viewModel(),
    changeInformationViewModel: ChangeInformationViewModel = viewModel()
){
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val userId = getUserId(context)
    if (userId != null) {
        resultStudyViewModel.setUserId(userId)
    } else {
        Log.e("DrawerContent", "User không tồn tại!")
    }

    val imageUrlOld by resultStudyViewModel.imageUrl.observeAsState("")
    val userNameOld by resultStudyViewModel.userName.observeAsState("")

    var name by remember { mutableStateOf("") }
    val nameFocusRequester = remember { FocusRequester() }
    var imageUrl by remember { mutableStateOf<Uri?>(null) }
    var isImageUpdated by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUrl = uri
            isImageUpdated = true
            Log.d("AddProductScreen", "Selected image URI: $uri") // Log selected URI
        }

    val errorMessage by changeInformationViewModel.errorMessage.observeAsState()
    val updateSuccess by changeInformationViewModel.updateSuccess.observeAsState(false)

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            changeInformationViewModel.resetErrorMessage()
        }
    }

    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            navController.popBackStack()
            Toast.makeText(context, "Đổi thông tin cá nhân thành công", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(userNameOld, imageUrlOld) {
        name = userNameOld ?: ""
    }

    LaunchedEffect(imageUrlOld) {
        if (!isImageUpdated) {
            imageUrl = if (imageUrlOld.isNotEmpty()) Uri.parse(imageUrlOld) else null
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
                Spacer(modifier = Modifier.height(10.dp))

                if (imageUrl != null) {
                    Image(
                        painter = rememberImagePainter(data = imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(130.dp)
                            .clickable { launcher.launch("image/*") }
                            .clip(RoundedCornerShape(20.dp))
                            .shadow(3.dp, RoundedCornerShape(20.dp))
                            .background(Color.White),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Button(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier
                            .width(130.dp)
                            .height(130.dp)
                            .shadow(3.dp, RoundedCornerShape(20.dp))
                            .background(Color.White),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = Color(0xFF0066c7),
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "Thêm ảnh",
                                color = Color(0xff303030),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
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
                value = name,
                onValueChange = { name = it },
                label = "Nhập tên",
                modifier = Modifier
                    .focusRequester(nameFocusRequester)
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            CustomBigButton(
                title = "Tiếp tục",
                onClick = {
                    changeInformationViewModel.updateUserInformation(name, imageUrl, isImageUpdated)
                }
            )
        }
    }
}