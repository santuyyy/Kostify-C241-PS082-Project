package com.example.kostify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kostify.data.api.response.BookmarkResponseItem
import com.example.kostify.data.api.response.CreatedAt
import com.example.kostify.data.api.response.KostDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FavoriteViewModel : ViewModel() {

    private val _bookmarks = MutableLiveData<List<BookmarkResponseItem>>()
    val bookmarks: LiveData<List<BookmarkResponseItem>> = _bookmarks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        fetchBookmarks()
    }

    fun fetchBookmarks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedBookmarks = getBookmarks()
                _bookmarks.value = fetchedBookmarks
            } catch (e: Exception) {
                _error.value = "Error fetching bookmarks: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Fungsi baru untuk memperbarui LiveData bookmarks
    fun updateBookmarks(newBookmarks: List<BookmarkResponseItem>) {
        _bookmarks.value = newBookmarks
    }

    private suspend fun getBookmarks(): List<BookmarkResponseItem> {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Handle the case where the user is not logged in
            return emptyList()
        }

        val bookmarksRef = db.collection("bookmarks")
            .whereEqualTo("userId", currentUser.uid)

        return try {
            val querySnapshot = bookmarksRef.get().await()
            querySnapshot.documents.mapNotNull { document ->
                val data = document.data

                // Safe cast with null check for 'createdAt' and 'kostDetails'
                val createdAtMap = data?.get("createdAt") as? Map<String, Any>
                val kostDetailsMap = data?.get("kostDetails") as? Map<String, Any>

                BookmarkResponseItem(
                    id = document.id,
                    kostId = data?.get("kostId") as? String ?: "",
                    userId = data?.get("userId") as? String ?: "",
                    createdAt = if (createdAtMap != null) CreatedAt(
                        seconds = createdAtMap["_seconds"] as? Int ?: 0,
                        nanoseconds = createdAtMap["_nanoseconds"] as? Int ?: 0
                    ) else CreatedAt(0, 0), // Provide a default CreatedAt if the map is null
                    kostDetails = if (kostDetailsMap != null) KostDetails(
                        gender = kostDetailsMap["gender"] as? String ?: "",
                        harga = (kostDetailsMap["harga"] as? Long ?: 0).toInt(),
                        namaKost = kostDetailsMap["nama_kost"] as? String ?: "",
                        deskripsi = kostDetailsMap["deskripsi"] as? String ?: "",
                        id = (kostDetailsMap["id"] as? Long ?: 0).toInt(),
                        luasKamar = kostDetailsMap["Luas kamar"] as? String ?: "",
                        alamat = kostDetailsMap["alamat"] as? String ?: ""
                    ) else KostDetails("", 0, "", "", 0, "", "") // Provide a default KostDetails if the map is null
                )
            }
        } catch (e: Exception) {
            // Handle any exceptions that may occur during the retrieval process
            emptyList()
        }
    }
}