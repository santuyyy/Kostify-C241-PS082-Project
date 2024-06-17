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
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailKosViewModel : ViewModel() {
/*class DetailKosViewModel (private val repository: DetailKosRepository) : ViewModel() {*/
    private val _detailKos = MutableLiveData<DetailKostResponse>()
    val detailKos: LiveData<DetailKostResponse> = _detailKos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isBookmarked = MutableLiveData(false)
    val isBookmarked: LiveData<Boolean> = _isBookmarked

    private val _bookmarkId = MutableLiveData("")
    val bookmarkId: LiveData<String> = _bookmarkId

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    private val auth: FirebaseAuth = Firebase.auth
    private val currentUserId: String? = auth.currentUser?.uid

//    ----------------------------------


    companion object {
        private const val TAG = "DetailKosViewModel"
    }


/*    fun addBookmark(kostId: Int) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val addBookmarkRequest = AddBookmarkRequest(userId, kostId.toString())
            val client = ApiConfig.getApiService().addBookmark(addBookmarkRequest)
            client.enqueue(object : Callback<AddBookmarkResponse> {
                override fun onResponse(
                    call: Call<AddBookmarkResponse>,
                    response: Response<AddBookmarkResponse>
                ) {
                    if (response.isSuccessful) {
                        _bookmarkState.value = true
                    } else {
                        Log.e(TAG, "addBookmark - onFailure: ${response.message()}")
                        _errorState.value = "Gagal menambahkan bookmark"
                    }
                }

                override fun onFailure(call: Call<AddBookmarkResponse>, t: Throwable) {
                    Log.e(TAG, "addBookmark - onFailure: ${t.message}")
                    _errorState.value = "Gagal menambahkan bookmark"
                }
            })
        }
    }*/

  /*  fun removeBookmark(bookmarkId: String) {
        viewModelScope.launch {
            val removeBookmarkRequest = RemoveBookmarkRequest(bookmarkId)
            val client = ApiConfig.getApiService().removeBookmark(removeBookmarkRequest)
            client.enqueue(object : Callback<RemoveBookmarkResponse> {
                override fun onResponse(
                    call: Call<RemoveBookmarkResponse>,
                    response: Response<RemoveBookmarkResponse>
                ) {
                    if (response.isSuccessful) {
                        _bookmarkState.value = false
                    } else {
                        Log.e(TAG, "removeBookmark - onFailure: ${response.message()}")
                        _errorState.value = "Gagal menghapus bookmark"
                    }
                }

                override fun onFailure(call: Call<RemoveBookmarkResponse>, t: Throwable) {
                    Log.e(TAG, "removeBookmark - onFailure: ${t.message}")
                    _errorState.value = "Gagal menghapus bookmark"
                }
            })
        }
    }*/


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
                         _detailKos.value = response.body()/*
                         _detailKos.value?.id?.let { kostId ->
                             checkBookmarkStatus(kostId.toString())
                         }*/
                     } else {
                         Log.e(TAG, "getDetailKos - onFailure: ${response.message()}")
                     }
                     _isLoading.value = false // Pindahkan pengaturan _isLoading ke sini
                 }

                 override fun onFailure(call: Call<DetailKostResponse>, t: Throwable) {
                     Log.e(TAG, "getDetailKos - onFailure: ${t.message}")
                     _isLoading.value = false // Pindahkan pengaturan _isLoading ke sini
                 }
             })
         }
     }

     /*  fun checkBookmarkStatus(kostId: String) {
           viewModelScope.launch {
               _isLoading.value = true
               currentUserId?.let { userId ->
                   val client = ApiConfig.getApiService().getBookmarks(userId)
                   client.enqueue(object : Callback<List<Bookmark>> {
                       override fun onResponse(
                           call: Call<List<Bookmark>>,
                           response: Response<List<Bookmark>>
                       ) {
                           _isLoading.value = false
                           if (response.isSuccessful) {
                               val bookmarks = response.body() ?: emptyList()
                               _isBookmarked.value = bookmarks.any { it.kostId == kostId }
                               if (_isBookmarked.value == true) {
                                   _bookmarkId.value = bookmarks.first { it.kostId == kostId }.bookmarkId
                               } else {
                                   _bookmarkId.value = ""
                               }
                           } else {
                               Log.e(TAG, "checkBookmarkStatus - onFailure: ${response.message()}")
                           }
                       }

                       override fun onFailure(call: Call<List<Bookmark>>, t: Throwable) {
                           _isLoading.value = false
                           Log.e(TAG, "checkBookmarkStatus - onFailure: ${t.message.toString()}")
                       }
                   })
               } ?: run {
                   _isLoading.value = false
                   // Tangani kasus ketika pengguna belum login, misalnya:
                   Log.d(TAG, "Pengguna belum login. Tidak dapat memeriksa status bookmark.")
               }
           }
       }

      fun addBookmark(kostId: String) {
          viewModelScope.launch {
              _isLoading.value = true
              currentUserId?.let { userId ->
                  val request = BookmarkRequest(userId, kostId)
                  val client = ApiConfig.getApiService().addBookmark(request)
                  client.enqueue(object : Callback<Void> {
                      override fun onResponse(call: Call<Void>, response: Response<Void>) {
                          _isLoading.value = false
                          if (response.isSuccessful) {
                              _isBookmarked.value = true
                              _toastMessage.value = "Kost ditambahkan ke bookmark"
                              // Anda mungkin perlu memperbarui _bookmarkId di sini jika API Anda mengembalikannya
                          } else {
                              _isBookmarked.value = false
                              Log.e(TAG, "addBookmark - onFailure: ${response.message()}")
                              _toastMessage.value = "Gagal menambahkan bookmark"
                          }
                      }

                      override fun onFailure(call: Call<Void>, t: Throwable) {
                          _isLoading.value = false
                          _isBookmarked.value = false
                          Log.e(TAG, "addBookmark - onFailure: ${t.message.toString()}")
                          _toastMessage.value = "Gagal menambahkan bookmark"
                      }
                  })
              } ?: run {
                  _isLoading.value = false
                  Log.d(TAG, "Pengguna belum login. Tidak dapat menambahkan bookmark.")
                  _toastMessage.value = "Silakan login untuk menambahkan bookmark"
              }
          }
      }

     fun removeBookmark(bookmarkId: String) {
         viewModelScope.launch {
             _isLoading.value = true
             val request = RemoveBookmarkRequest(bookmarkId)
             val client = ApiConfig.getApiService().removeBookmark(request)
             client.enqueue(object : Callback<Void> {
                 override fun onResponse(call: Call<Void>, response: Response<Void>) {
                     _isLoading.value = false
                     if (response.isSuccessful) {
                         _isBookmarked.value = false
                         _bookmarkId.value = ""
                         _toastMessage.value = "Bookmark dihapus"
                     } else {
                         _isBookmarked.value = true
                         Log.e(TAG, "removeBookmark - onFailure: ${response.message()}")
                         _toastMessage.value = "Gagal menghapus bookmark"
                     }
                 }

                 override fun onFailure(call: Call<Void>, t: Throwable) {
                     _isLoading.value = false
                     _isBookmarked.value = true
                     Log.e(TAG, "removeBookmark - onFailure: ${t.message.toString()}")
                     _toastMessage.value = "Gagal menghapus bookmark"
                 }
             })
         }
     }*/

//    -----------------------------


 /*   fun onToastMessageShown() {
        _toastMessage.value = null
    }

    fun getDetailKos(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getDetailKos(id) { result ->
                _isLoading.value = false
                _detailKos.value = result
                if (result is Result.Success) {
                    val kostId = result.data.id.toString()
                    checkBookmarkStatus(kostId)
                }
            }
        }
    }

    fun getBookmarks() {
        viewModelScope.launch {
            repository.getBookmarks { result ->
                _bookmarks.value = result
            }
        }
    }

    fun addBookmark(kostId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.addBookmark(kostId) { result ->
                _isLoading.value = false
                when (result) {
                    is Result.Success -> {
                        _isBookmarked.value = true
                        _toastMessage.value = result.data.message
                        refreshBookmarks()
                    }
                    is Result.Failure -> {
                        _isBookmarked.value = false
                        Log.e(TAG, "addBookmark - onFailure: ${result.exception.message}")
                        _toastMessage.value = "Gagal menambahkan bookmark"
                    }
                }
            }
        }
    }

    fun removeBookmark(bookmarkId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.removeBookmark(bookmarkId) { result ->
                _isLoading.value = false
                when (result) {
                    is Result.Success -> {
                        _isBookmarked.value = false
                        _toastMessage.value = result.data.message
                        refreshBookmarks()
                    }
                    is Result.Failure -> {
                        Log.e(TAG, "removeBookmark - onFailure: ${result.exception.message}")
                        _toastMessage.value = "Gagal menghapus bookmark"
                    }
                }
            }
        }
    }

    private fun checkBookmarkStatus(kostId: String) {
        viewModelScope.launch {
            repository.getBookmarks { result ->
                if (result is Result.Success) {
                    _isBookmarked.value = result.data.bookmarkResponse?.any { it?.kostId == kostId } ?: false
                }
            }
        }
    }

    private fun refreshBookmarks() {
        getBookmarks()
    }*/

}