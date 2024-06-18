package com.example.kostify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kostify.data.api.response.DetailKostResponse
import com.example.kostify.data.api.retrofit.ApiConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*class DetailKosViewModel (private val repository: DetailKosRepository) : ViewModel() {*/
class DetailKosViewModel : ViewModel() {

    private val _detailKos = MutableLiveData<DetailKostResponse>()
    val detailKos: LiveData<DetailKostResponse> = _detailKos

    private val _isBookmarked = MutableLiveData<Boolean>()
    val isBookmarked: LiveData<Boolean> = _isBookmarked

    private val _bookmarkStatusMessage = MutableLiveData<String>()
    val bookmarkStatusMessage: LiveData<String> = _bookmarkStatusMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val currentUserId: String? = auth.currentUser?.uid

    companion object {
        private const val TAG = "DetailKosViewModel"
    }

    fun getDetailKos(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getDetailKost(id)
            client.enqueue(object : Callback<DetailKostResponse> {
                override fun onResponse(
                    call: Call<DetailKostResponse>,
                    response: Response<DetailKostResponse>
                ) {
                    if (response.isSuccessful) {
                        _detailKos.value = response.body()
                        checkBookmarkStatus(id)
                    } else {
                        Log.e(TAG, "getDetailKos - onFailure: ${response.message()}")
                    }
                    _isLoading.value = false
                }

                override fun onFailure(call: Call<DetailKostResponse>, t: Throwable) {
                    Log.e(TAG, "getDetailKos - onFailure: ${t.message}")
                    _isLoading.value = false
                }
            })
        }
    }

    fun checkBookmarkStatus(kostId: Int) {
        if (checkLoginStatus()) {
            val userId = auth.currentUser!!.uid
            db.collection("bookmarks")
                .whereEqualTo("userId", userId)
                .whereEqualTo("kostId", kostId.toString())
                .get()
                .addOnSuccessListener { querySnapshot ->
                    _isBookmarked.value = !querySnapshot.isEmpty
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "checkBookmarkStatus - onFailure: ${e.message}")
                    // Anda mungkin ingin menangani kesalahan di sini, misalnya dengan menampilkan pesan kepada pengguna
                }
        } else {
            _isBookmarked.value = false // Pengguna belum login, jadi tidak mungkin ada bookmark
        }
    }

    fun toggleBookmark(kostId: Int) {
        if (checkLoginStatus()) {
            val userId = auth.currentUser!!.uid
            val kostDetails = detailKos.value?.toHashMap() ?: return

            if (isBookmarked.value == true) {
                // Hapus bookmark
                removeBookmark(userId, kostId.toString())
            } else {
                // Tambah bookmark
                addBookmark(userId, kostId.toString(), kostDetails as Map<String, Any>)
            }
        } else {
            _bookmarkStatusMessage.value = "Anda harus login untuk menambahkan bookmark."
        }
    }

    fun DetailKostResponse.toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "gender" to gender,
            "harga" to harga,
            "nama_kost" to namaKost,
            "deskripsi" to deskripsi,
            "id" to id,
            "Luas kamar" to luasKamar,
            "alamat" to alamat
        )
    }

    private fun checkLoginStatus(): Boolean {
        return auth.currentUser != null
    }

    private fun addBookmark(userId: String, kostId: String, kostDetails: Map<String, Any>) {
        val bookmarkRef = db.collection("bookmarks").document()
        val bookmarkData = hashMapOf(
            "userId" to userId,
            "kostId" to kostId,
            "kostDetails" to kostDetails,
            "createdAt" to FieldValue.serverTimestamp()
        )
        bookmarkRef.set(bookmarkData)
            .addOnSuccessListener {
                _isBookmarked.value = true
                _bookmarkStatusMessage.value = "Bookmark ditambahkan."
            }
            .addOnFailureListener { e ->
                _bookmarkStatusMessage.value = "Gagal menambahkan bookmark: ${e.message}"
            }
    }

    private fun removeBookmark(userId: String, kostId: String) {
        db.collection("bookmarks")
            .whereEqualTo("userId", userId)
            .whereEqualTo("kostId", kostId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot) {
                        document.reference.delete()
                            .addOnSuccessListener {
                                _isBookmarked.value = false
                                _bookmarkStatusMessage.value = "Bookmark dihapus."
                            }
                            .addOnFailureListener { e ->
                                _bookmarkStatusMessage.value = "Gagal menghapus bookmark: ${e.message}"
                            }
                    }
                } else {
                    _bookmarkStatusMessage.value = "Bookmark tidak ditemukan."
                }
            }
            .addOnFailureListener { e ->
                _bookmarkStatusMessage.value = "Gagal menghapus bookmark: ${e.message}"
            }
    }
}



