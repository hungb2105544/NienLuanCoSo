package com.example.music.entities

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music.other.Constants.SONG_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SongViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _song = MutableStateFlow<Song?>(null)
    val song: StateFlow<Song?> get() = _song

    private val _currentSongIndex = MutableStateFlow(0)
    val currentSongIndex: StateFlow<Int> get() = _currentSongIndex

    private val _songList = MutableStateFlow<List<Song>>(emptyList())
    val songList: StateFlow<List<Song>> = _songList.asStateFlow()


    private var allSongs = listOf<Song>()


    fun fetchSongById(id: String) {
        viewModelScope.launch {
            _song.value = getSong(id)
        }
    }

    private suspend fun getSong(id: String): Song? {
        return try {
            val songCollection = firestore.collection(SONG_COLLECTION)
            val querySnapshot = songCollection.whereEqualTo("id", id).get().await()
            querySnapshot.documents.firstOrNull()?.toObject(Song::class.java)
        } catch (e: Exception) {
            Log.e("SongViewModel", "Error fetching song: ${e.message}")
            null
        }
    }


    private suspend fun fetchAllSongsFromFirestore(): List<Song> {
        return try {
            val songCollection = firestore.collection(SONG_COLLECTION)
            val querySnapshot = songCollection.get().await()
            querySnapshot.documents.mapNotNull { document ->
                document.toObject(Song::class.java)
            }
        } catch (e: Exception) {
            Log.e("SongViewModel", "Error fetching songs: ${e.message}")
            emptyList()
        }
    }

    fun fetchSongsByCurrentSong(currentSongId: String) {
        viewModelScope.launch {
            val songs = fetchAllSongsFromFirestore()
            allSongs = songs // Cập nhật allSongs
            _songList.value = songs
            _currentSongIndex.value = songs.indexOfFirst { it.id == currentSongId }
        }
    }

    fun searchSongs(query: String) {
        // Kiểm tra nếu allSongs chưa được tải, thực hiện tải
        if (allSongs.isEmpty()) {
            viewModelScope.launch {
                val songs = fetchAllSongsFromFirestore()
                allSongs = songs
                filterSongs(query)
            }
        } else {
            filterSongs(query)
        }
    }

    private fun filterSongs(query: String) {
        _songList.value = if (query.isEmpty()) {
            allSongs
        } else {
            allSongs.filter { song ->
                song.nameMusic.contains(query, ignoreCase = true) // Tìm kiếm theo tên bài hát
            }
        }
    }

    // Chuyển tiếp bài hát
    val songs: StateFlow<List<Song>> = _songList
    private var currentIndex = 0
    fun nextSong() {
        if (currentIndex < _songList.value.lastIndex) {
            currentIndex++
            _song.value = _songList.value[currentIndex]
        }
    }

    fun previousSong() {
        if (currentIndex > 0) {
            currentIndex--
            _song.value = _songList.value[currentIndex]
        }
    }
}

