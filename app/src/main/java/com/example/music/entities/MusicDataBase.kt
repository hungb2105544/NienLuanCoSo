package com.example.music.entities

import com.example.music.other.Constants.SONG_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MusicDataBase {

    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(SONG_COLLECTION)

    suspend fun getAllSongs(): List<Song>? {
        return try {
            val result = songCollection.get().await()
            result.documents.mapNotNull { document ->
                document.toObject(Song::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

}
