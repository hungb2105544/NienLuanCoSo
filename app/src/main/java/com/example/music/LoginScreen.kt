package com.example.music

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth


@Composable
fun Login(
    isLoginSuccess: Boolean ,
    navController: NavController,
    openHome: () -> Unit,
    openSearch: () -> Unit,
    openProfile: () -> Unit
) {
    // Biến trạng thái để quản lý việc hiển thị thông báo lỗi và thành công
    var isLoginSuccessful by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Firebase Authentication instance
    val auth = FirebaseAuth.getInstance()
    Scaffold(
        bottomBar = {
            BottomBar(
                openHome = openHome,
                openSearch = openSearch,
                openProfile = openProfile
            )
        }
    ){paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "SIGN IN",
                fontSize = 45.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(20.dp))

            // USERNAME
            var email by remember { mutableStateOf("") }
            OutlinedTextField(
                label = { Text("Email") },
                shape = RoundedCornerShape(200.dp),
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // PASSWORD
            var password by remember { mutableStateOf("") }
            OutlinedTextField(
                label = { Text("Password") },
                shape = RoundedCornerShape(200.dp),
                visualTransformation = PasswordVisualTransformation(),
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Nút đăng nhập
            Button(
                onClick = {
                    // Reset lại trạng thái
                    isLoginSuccessful = false
                    showError = false
                    errorMessage = ""

                    // Kiểm tra nếu email và mật khẩu không rỗng
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        // Sử dụng Firebase để đăng nhập
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Đăng nhập thành công
                                    isLoginSuccessful = true
                                    showError = false
                                    isLoginSuccessful.toString()
                                    Log.d("TestLogin","$isLoginSuccessful")
                                    navController.navigate("Home/${isLoginSuccessful}")
                                } else {
                                    // Đăng nhập thất bại
                                    isLoginSuccessful = false
                                    showError = true
                                    errorMessage = task.exception?.message ?: "Lỗi không xác định"
                                }
                            }
                    } else {
                        // Nếu trường email hoặc password trống
                        showError = true
                        errorMessage = "Email và mật khẩu không được để trống!"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(200.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFFFFEC), // Vàng chanh
                                    Color(0xFFF6EACB), // Xanh biển nhạt
                                    Color(0xFFFFF6EA)  // Hồng nổi bật
                                )
                            )
                        )
                        .border(
                            width = 4.dp, // Độ dày của viền
                            color = Color.Gray, // Màu của viền
                            shape = RoundedCornerShape(200.dp) // Hình dạng của viền
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Login",
                        fontSize = 30.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Thông báo đăng nhập thành công
            if (isLoginSuccessful) {
                Text(text = "Đăng nhập thành công!", color = MaterialTheme.colorScheme.primary)
            }
            // Thông báo lỗi đăng nhập
            if (showError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

}

