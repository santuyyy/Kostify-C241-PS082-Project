package com.example.kostify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kostify.data.api.response.ItemsItem
import com.example.kostify.data.api.response.KostResponse
import com.example.kostify.data.api.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchKosViewModel : ViewModel() {

    private val _listKos = MutableLiveData<List<ItemsItem?>?>()
    val listKos : LiveData<List<ItemsItem?>?> = _listKos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "SearchKosViewModel"
    }

    fun findKostSearch(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchKost(query)

        client.enqueue(object : Callback<KostResponse> {
            override fun onResponse(
                call: Call<KostResponse>,
                response: Response<KostResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listKos.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<KostResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}