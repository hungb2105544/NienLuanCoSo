package com.example.music

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ProfileScreen(
    isLoginSuccess: Boolean,
    navController: NavController,
    openHome: () -> Unit,
    openSearch: () -> Unit,
    openProfile: () -> Unit,
    openLogin: () -> Unit,
    openRegister: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomBar(
                openHome = openHome,
                openSearch = openSearch,
                openProfile = openProfile
            )
        }
    ) { paddingValues ->
        Log.d("Navigation", "check : $isLoginSuccess")

        if (isLoginSuccess) {
            LoggedInContent(paddingValues, navController)
        } else {
            NotLoggedInContent(paddingValues, openLogin, openRegister)
        }
    }
}

@Composable
fun LoggedInContent(paddingValues: PaddingValues, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 100.dp,
                start = 20.dp,
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current)
            )
    ) {
        item {
            Text(
                text = "Bạn đã đăng nhập\nthành công",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
        item {
            Button(onClick = { navController.navigate("Home/false") }) {
                Text(text = "Đăng xuất")
            }
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
        item {
            ExpandableSection(
                title = "Giới thiệu Ứng Dụng Nghe Nhạc",
                content = "Chào mừng bạn đến với N4, ứng dụng nghe nhạc hàng đầu dành cho những tín đồ yêu âm nhạc! Với N4, bạn sẽ được trải nghiệm thế giới âm nhạc phong phú và đa dạng, từ những bản hit mới nhất đến những giai điệu cổ điển bất hủ."
            )
        }
        item {
            ExpandableSection(
                title = "Chính sách thu thập dữ liệu",
                content = "Liệt kê các loại dữ liệu được thu thập (ví dụ: tên, địa chỉ email, vị trí)\nGiải thích lý do thu thập từng loại dữ liệu."
            )
        }
        item {
            ExpandableSection(
                title = "Chính sách cập nhật",
                content = "Thông báo cho người dùng về bất kỳ thay đổi nào trong chính sách hoặc ứng dụng.\nCung cấp thông tin về cách người dùng có thể theo dõi các thay đổi này."
            )
        }
        item {
            ExpandableSection(
                title = "Chính sách quyền lợi người dùng",
                content = "Nêu rõ quyền của người dùng đối với dữ liệu của họ.\nCung cấp hướng dẫn về cách người dùng có thể thực hiện quyền của mình."
            )
        }
        item {
            ExpandableSection(
                title = "Tác giả",
                content = "Nhà phát triển: Lý Quốc Hùng\nVersion: 1.0\nNgày phát hành: 04/12/2025"
            )
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun NotLoggedInContent(
    paddingValues: PaddingValues,
    openLogin: () -> Unit,
    openRegister: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = "Bạn chưa đăng nhập ??",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Nếu chưa có tài khoản bạn có thể đăng ký !",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        LoginRegisterButtons(openLogin, openRegister)
    }
}

@Composable
fun LoginRegisterButtons(openLogin: () -> Unit, openRegister: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = openLogin) {
            Text(text = "Đăng nhập", fontSize = 15.sp)
        }
        Text(
            modifier = Modifier.padding(5.dp),
            textAlign = TextAlign.Center,
            text = "or"
        )
        Button(onClick = openRegister) {
            Text(text = "Đăng ký", fontSize = 15.sp)
        }
    }
}

@Composable
fun ExpandableSection(title: String, content: String) {
    // Tạo biến trạng thái để lưu trạng thái mở rộng hay thu gọn của đề mục
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded } // Khi nhấn, đổi trạng thái
            .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
    ) {
        // Hiển thị tiêu đề đề mục
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
        )

        // Sử dụng AnimatedVisibility để hiển thị nội dung khi mở rộng
        AnimatedVisibility(visible = isExpanded) {
            Text(
                text = content,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
