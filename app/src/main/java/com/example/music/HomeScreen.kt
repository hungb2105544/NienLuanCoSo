package com.example.music

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.chillmusic.R


@Composable
fun HomeScreen(
    isLoginSuccess: Boolean,
    openHome: () -> Unit,
    openSearch: () -> Unit,
    openProfile: () -> Unit,
    openMusicProfile: (String) -> Unit,
    viewModel: MusicViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Log.d("Home", "$isLoginSuccess")

    LaunchedEffect(Unit) {
        viewModel.fetchSongs()
    }
    // val isLoading by viewModel.isLoading.collectAsState()
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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "CHILL MUSIC",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_cast),
                    contentDescription = "Cast",
                    modifier = Modifier.padding(10.dp)
                )
            }

            LazyRow(
                modifier = Modifier.padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Nếu danh sách bài hát đã được tải
                viewModel.songs?.let { songs ->
                    items(songs.take(10)) { song ->
                        Card(
                            open = { openMusicProfile(song.id) },
                            contentDescription = song.authorMusic,
                            ImageID = song.imageUrl,
                            name = song.nameMusic,
                            id = song.id
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "List Song",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 10.dp)
            )
            LazyColumn {
                viewModel.songs?.let { songs ->
                    items(songs) { song ->
                        searchItem(song, openMusicProfile)
                    }
                }
            }

        }
    }
}


@Composable
fun Card(
    contentDescription: String = "Pháp Kiều, Quân A.P, RHYDER",
    name: String = "Hào Quang",
    ImageID: String = "",
    id: String = "",
    open: () -> Unit
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFEC),
        ),
        border = BorderStroke(2.dp, Color.Black),
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .size(width = 150.dp, height = 200.dp)
            .clickable {
                open()
            }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = name,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 10.dp)
                    .align(Alignment.Start),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 15.sp,
                maxLines = 1
            )
            Text(
                maxLines = 1,
                overflow = TextOverflow.Clip,
                text = contentDescription,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
                textAlign = TextAlign.Start,
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp
            )
        }
        AsyncImage(
            model = ImageID,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
    }
}

