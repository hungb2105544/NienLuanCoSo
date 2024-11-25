package com.example.music

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.music.BottomBar
import com.example.music.Navigation
import com.example.music.entities.Song
import com.example.music.entities.SongViewModel

@Composable
fun Search(
    isLoginSuccess: Boolean,
    openHome: () -> Unit,
    openSearch: () -> Unit,
    openProfile: () -> Unit,
    openMusicProfile: (String) -> Unit,
    viewModel: SongViewModel = viewModel() // Use ViewModel
) {
    Log.d("Search","$isLoginSuccess")
    var searchItem by remember { mutableStateOf("") }
    val songList by viewModel.songList.collectAsState() // Observe the song list

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Navigation()
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                // Search field
                OutlinedTextField(
                    label = {
                        Text(
                            text = "Search",
                            fontSize = 10.sp
                        )
                    },
                    shape = RoundedCornerShape(100f),
                    value = searchItem,
                    onValueChange = {
                        searchItem = it
                        if (searchItem.isNotEmpty()) {
                            viewModel.searchSongs(searchItem) // Call search method in ViewModel
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Spacer(modifier = Modifier.height(16.dp))
                // Search results list
                LazyColumn {
                    items(songList) { song ->
                        searchItem(song, openMusicProfile) // Correctly call searchItem
                    }
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
fun searchItem(
    song: Song,
    openMusicProfile: (String) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(start = 10.dp)
            .clickable { openMusicProfile(song.id) }
    ) {
        Column(
            modifier = Modifier
                .size(60.dp, 60.dp)
                .padding(5.dp)
        ) {
            AsyncImage(
                model = song.smallImage,
                contentDescription = "anh",
                contentScale = ContentScale.FillBounds
            )
        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = song.nameMusic, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = song.authorMusic, fontSize = 10.sp)
        }
    }
}
