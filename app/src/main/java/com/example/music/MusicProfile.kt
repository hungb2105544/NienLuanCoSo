package com.example.music

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.chillmusic.R
import com.example.music.entities.Song
import com.example.music.entities.SongViewModel
import kotlinx.coroutines.delay
import java.io.IOException

@Composable
fun MusicProfileScreen(
    isLoginSuccess: Boolean,
    id: String,  // id của bài hát
    openHome: () -> Unit,
    openSearch: () -> Unit,
    openProfile: () -> Unit,
    viewModel: SongViewModel = viewModel()  // Sử dụng viewModel() để đảm bảo trạng thái được giữ
) {
    Log.d("MusicProfile","$isLoginSuccess")
    val songState by viewModel.song.collectAsState()  // Lắng nghe thay đổi của song
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    // Gọi hàm để lấy dữ liệu bài hát khi UI được dựng lên
    LaunchedEffect(id) {
        viewModel.fetchSongById(id)
        viewModel.fetchSongsByCurrentSong(id)// Fetch song from Firestore via ViewModel
    }

    Scaffold(
        topBar = {
            Navigation()
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(16.dp)  // Điều chỉnh padding
            ) {
                // Nếu bài hát tồn tại, hiển thị thông tin
                songState?.let { song ->  // Check if song exists in state
                    item {
                        Box(
                            modifier = Modifier
                                .size(screenWidth.dp, screenWidth.dp)
                                .clip(RoundedCornerShape(30.dp))  // Điều chỉnh shape
                        ) {
                            AsyncImage(
                                model = song.imageUrl,  // Đặt ảnh theo dữ liệu nếu có
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = song.nameMusic,  // Sử dụng tên bài hát
                                contentScale = ContentScale.FillBounds
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black
                                            ),
                                            startY = 5f
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, bottom = 24.dp)  // Điều chỉnh padding
                            ) {
                                Column(modifier = Modifier.padding(start = 10.dp)) {
                                    Text(
                                        text = song.nameMusic,  // Tên bài hát từ dữ liệu
                                        textAlign = TextAlign.Start,
                                        fontSize = 20.sp,  // Điều chỉnh kích thước chữ
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Clip
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))  // Điều chỉnh khoảng cách
                                    Text(
                                        text = song.authorMusic,  // Tên tác giả từ dữ liệu
                                        textAlign = TextAlign.Start,
                                        fontSize = 16.sp,  // Điều chỉnh kích thước chữ
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Clip
                                    )
                                }
                            }
                        }
                    }
                    item {
                        MusicPlayer(song = song, viewModel = viewModel)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }  // Điều chỉnh khoảng cách
                item {
                    //    Text(text = song.musicUrl)
                    CardSubmiiter()
                }
            }
        },
        bottomBar = {
            BottomBar(
                openHome = openHome,
                openSearch = openSearch,
                openProfile = openProfile
            )
        },
    )
}

@Composable
fun Navigation() {
    Box(
        modifier = Modifier
            .background(color = Color.Black)
            .fillMaxWidth()
            .height(56.dp)
    ) {
    }
}

data class IconItem(val iconName: String, val icon: Int, val open: () -> Unit)

@Composable
fun BottomBar(
    openHome: () -> Unit,
    openSearch: () -> Unit,
    openProfile: () -> Unit
) {
    val iconBar = listOf(
        IconItem("Home", R.drawable.ic_home, open = openHome),
        IconItem("Search", R.drawable.ic_search, open = openSearch),
        IconItem("Profile", R.drawable.ic_profile, open = openProfile)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(color = Color.Black),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        for (iconItem in iconBar) {
            NavigationItem(
                icon = iconItem.icon,
                iconName = iconItem.iconName,
                onClick = iconItem.open
            )
        }
    }
}

@Composable
fun NavigationItem(
    icon: Int,
    iconName: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(10.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = iconName,
            alignment = Alignment.Center,
            modifier = Modifier.size(30.dp, 30.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = iconName, fontSize = 10.sp, color = Color.White)
    }
}

@Composable
fun CardSubmiiter(
    nameSubmiter: String = "Ly Quoc Hung",
    isOnline: Boolean = true,
    subscriber: Int = 0,
    imageAvatar: Int = R.drawable.avatar
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 10.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = imageAvatar),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop, // Giữ tỉ lệ hình ảnh
                modifier = Modifier.size(60.dp, 60.dp)
            )
            Column(
                modifier = Modifier
                    .padding(start = 6.dp, top = 6.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = nameSubmiter, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "$subscriber Subscriber")
            }
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd),
            contentAlignment = Alignment.Center
        ) {
            var isClick by remember { mutableStateOf(false) }
            Button(
                onClick = { isClick = true },
                modifier = Modifier.size(60.dp, 30.dp),
                shape = RoundedCornerShape(15f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isClick) Color(0xFF4CAF50) else Color(0xFFFF9874),
                    contentColor = Color.White
                )
            ) {
                Text(text = "+", color = Color.White)
            }
        }
    }
}

@Composable
fun getNavigationBarHeight(): Dp {
    val view = LocalView.current
    val insets = ViewCompat.getRootWindowInsets(view)
    val navigationBarHeight =
        insets?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom ?: 0
    return with(LocalDensity.current) { navigationBarHeight.toDp() }
}

@SuppressLint("DefaultLocale")
@Composable
fun MusicPlayer(song: Song, viewModel: SongViewModel) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentTime by remember { mutableFloatStateOf(0f) }
    var duration by remember { mutableIntStateOf(0) }
    val mediaPlayer = remember { MediaPlayer() }
    var isPreparing by remember { mutableStateOf(false) }
    // Tải mediaPlayer với URL của bài hát hiện tại
    LaunchedEffect(song.musicUrl) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(song.musicUrl)
            isPreparing = true
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                duration = mediaPlayer.duration
                isPreparing = false
                if (isPlaying) {
                    mediaPlayer.start()
                }
            }

            // Xử lý hoàn tất bài hát
            mediaPlayer.setOnCompletionListener {
                viewModel.nextSong()
                currentTime = 0f // Reset bộ đếm thời gian khi bài hát kết thúc
                val nextSong = viewModel.songList.value.getOrNull(viewModel.currentSongIndex.value)
                if (nextSong != null) {
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(nextSong.musicUrl)
                    isPreparing = true
                    mediaPlayer.prepareAsync()
                    isPlaying = true // Tự động phát bài hát tiếp theo
                }
            }
        } catch (e: IOException) {
            Log.e("MusicPlayer", "Lỗi khi tải nhạc từ URL: ${e.message}")
        }
    }

    // Cập nhật thời gian hiện tại khi đang phát
    LaunchedEffect(isPlaying) {
        if (isPlaying && !isPreparing) {
            mediaPlayer.start()
            while (mediaPlayer.isPlaying) {
                currentTime = mediaPlayer.currentPosition.toFloat()
                delay(1000)
            }
        } else if (!isPreparing) {
            mediaPlayer.pause()
        }
    }

    // Giao diện người dùng
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Bây giờ đang phát: ${song.nameMusic}")
        Spacer(modifier = Modifier.height(10.dp))

        // Kiểm tra trạng thái tải
        if (isPreparing) {
            Text(text = "Đang tải...", modifier = Modifier.align(Alignment.CenterHorizontally))
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Slider(
                value = currentTime,
                onValueChange = { newValue ->
                    currentTime = newValue
                    mediaPlayer.seekTo(newValue.toInt())
                },
                valueRange = 0f..duration.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    viewModel.previousSong()
                    currentTime = 0f // Reset bộ đếm thời gian khi chuyển bài hát
                    val prevSongIndex = viewModel.currentSongIndex.value
                    val prevSong = viewModel.songList.value.getOrNull(prevSongIndex)
                    if (prevSong != null) {
                        mediaPlayer.reset()
                        mediaPlayer.setDataSource(prevSong.musicUrl)
                        isPreparing = true
                        mediaPlayer.prepareAsync()
                    }
                    isPlaying = false
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Quay lại"
                    )
                }

                IconButton(onClick = {
                    isPlaying = !isPlaying
                }) {
                    if (isPlaying) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pause),
                            contentDescription = "Tạm dừng"
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = "Phát"
                        )
                    }
                }

                IconButton(onClick = {
                    viewModel.nextSong()
                    currentTime = 0f // Reset bộ đếm thời gian khi chuyển bài hát
                    val nextSong =
                        viewModel.songList.value.getOrNull(viewModel.currentSongIndex.value)
                    if (nextSong != null) {
                        mediaPlayer.reset()
                        mediaPlayer.setDataSource(nextSong.musicUrl)
                        isPreparing = true
                        mediaPlayer.prepareAsync()
                    }
                    isPlaying = false
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_next),
                        contentDescription = "Tiếp theo"
                    )
                }
            }

            Text(
                text = String.format(
                    "%d:%02d / %d:%02d",
                    (currentTime / 1000 / 60).toInt(),
                    (currentTime / 1000 % 60).toInt(),
                    (duration / 1000 / 60),
                    (duration / 1000 % 60)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }
    }

    // Giải phóng MediaPlayer khi composable bị hủy
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
}

