package com.example.quizizz_app.activities.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizizz_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
        label = {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight(400),
                color = Color(0xff44474f)
            )
        },
        textStyle = TextStyle(
            color = Color(0xff303030),
            fontWeight = FontWeight(400),
            fontSize = 16.sp
        ),
        singleLine = true,
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onValueChange("")
                    }
                ) {
                    Icon(
                        Icons.Outlined.Close, contentDescription = "",
                        modifier = Modifier.size(20.dp),
                        tint = Color(0xff4e4c58)
                    )
                }
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color(0xffb6b6b6),
            focusedBorderColor = Color(0xff005595),
            unfocusedLabelColor = Color(0xffb6b6b6),
            focusedLabelColor = Color(0xff005595)
        ),
        shape = RoundedCornerShape(11.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextFieldPassword(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White),
        label = {
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color(0xff44474f)
            )
        },
        textStyle = TextStyle(
            color = Color(0xff303030),
            fontWeight = FontWeight(400),
            fontSize = 16.sp
        ),
        singleLine = true,
        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            Row {
                if (value.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onValueChange("")
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Close, contentDescription = "",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xff4e4c58)
                        )
                    }
                }

                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Image(
                        painter = painterResource(id = if (passwordVisible) R.drawable.eye_off  else R.drawable.eye),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color(0xffb6b6b6),
            focusedBorderColor = Color(0xff005595),
            unfocusedLabelColor = Color(0xffb6b6b6),
            focusedLabelColor = Color(0xff005595)
        ),
        shape = RoundedCornerShape(11.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isFocused: MutableState<Boolean>,
    leadingIcon: @Composable (() -> Unit)? = null // Optional icon parameter
) {
    Spacer(modifier = Modifier.padding(top = 10.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, fontSize = 16.sp) },
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .onFocusChanged { isFocused.value = it.isFocused },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        leadingIcon = leadingIcon, // Add leadingIcon parameter
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xff005595),
            unfocusedBorderColor = Color(0xFFD2D2D2),
            containerColor = Color.Transparent
        )
    )
}