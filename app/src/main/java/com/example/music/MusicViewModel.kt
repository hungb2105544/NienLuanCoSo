package com.example.music

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music.entities.MusicDataBase
import com.example.music.entities.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MusicViewModel : ViewModel() {
    var songs: List<Song>? = null
        private set
    private val musicDataBase = MusicDataBase()

    fun fetchSongs() {
        viewModelScope.launch {
            songs = musicDataBase.getAllSongs()
        }
    }

}
