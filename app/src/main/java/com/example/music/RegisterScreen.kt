package com.example.music

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
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Composable
fun RegisterForm(
    isLoginSuccess: Boolean = false,
    navController: NavController,
    openHome: () -> Unit,
    openSearch: () -> Unit,
    openProfile: () -> Unit
) {
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().reference

    Scaffold(
        bottomBar = {
            BottomBar(
                openHome = openHome,
                openSearch = openSearch,
                openProfile = openProfile
            )
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
                .fillMaxSize()
        ) {
            HeaderRsgister()
            Spacer(modifier = Modifier.height(20.dp))
            UsernameField(username) { username = it }
            Spacer(modifier = Modifier.height(20.dp))
            PasswordField(password) { password = it }
            Spacer(modifier = Modifier.height(20.dp))
            EmailField(email) { email = it }
            Spacer(modifier = Modifier.height(20.dp))
            RegisterButton(
                username,
                password,
                email,
                auth,
                database,
                navController,
                isLoginSuccess,
                { errorMessage = it },
                { successMessage = it })
            Spacer(modifier = Modifier.height(16.dp))
            MessageText(errorMessage, successMessage)
        }
    }
}

@Composable
fun HeaderRsgister() {
    Text(
        text = "SIGN UP",
        fontSize = 45.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic
    )
}

@Composable
fun UsernameField(username: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    OutlinedTextField(
        label = { Text("Username") },
        value = username,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(200.dp),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { /* Handle done action */ })
    )
}

@Composable
fun PasswordField(password: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    OutlinedTextField(
        label = { Text("Password") },
        value = password,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(200.dp),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { /* Handle done action */ })
    )
}

@Composable
fun EmailField(email: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    OutlinedTextField(
        label = { Text("Email") },
        value = email,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(200.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.None
        ),
        keyboardActions = KeyboardActions(onDone = { /* Handle done action */ })
    )
}

@Composable
fun RegisterButton(
    username: TextFieldValue,
    password: TextFieldValue,
    email: TextFieldValue,
    auth: FirebaseAuth,
    database: DatabaseReference,
    navController: NavController,
    isLoginSuccess: Boolean,
    onError: (String) -> Unit,
    onSuccess: (String) -> Unit
) {
    Button(
        onClick = {
            onError("")
            onSuccess("")
            if (username.text.isNotEmpty() && password.text.isNotEmpty() && email.text.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email.text, password.text)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            val user = User(username.text, email.text)

                            if (userId != null) {
                                database.child("users").child(userId).setValue(user)
                                    .addOnSuccessListener {
                                        onSuccess("Đăng ký thành công!")
                                    }
                                    .addOnFailureListener {
                                        onError("Lỗi khi lưu thông tin người dùng!")
                                    }
                            }
                            navController.navigate("Login")
                        } else {
                            onError("Đăng ký thất bại: ${task.exception?.message}")
                        }
                    }
            } else {
                onError("Vui lòng nhập đầy đủ thông tin!")
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
                            Color(0xFFFFFFEC),
                            Color(0xFFF6EACB),
                            Color(0xFFFFF6EA)
                        )
                    )
                )
                .border(
                    width = 4.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(200.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Register",
                fontSize = 30.sp,
                fontStyle = FontStyle.Italic,
                color = Color.Black
            )
        }
    }
}

@Composable
fun MessageText(errorMessage: String, successMessage: String) {
    if (errorMessage.isNotEmpty()) {
        Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
    }

    if (successMessage.isNotEmpty()) {
        Text(text = successMessage, color = MaterialTheme.colorScheme.primary)
    }
}

data class User(val username: String? = null, val email: String? = null)