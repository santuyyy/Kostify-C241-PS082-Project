package com.example.kostify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kostify.data.api.response.RandomKostResponse
import com.example.kostify.data.api.response.RandomkostitemItem
import com.example.kostify.data.api.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeScreenViewModel : ViewModel() {

    private val _listRandomKos = MutableLiveData<List<RandomkostitemItem?>?>()
    val listRandomKos : LiveData<List<RandomkostitemItem?>?> = _listRandomKos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        const val TAG = "HomeScreenViewModel"
    }

    init {
        getRandomKos()
    }

    private fun getRandomKos() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getRandomKos()
        client.enqueue(object : Callback<RandomKostResponse> {
            override fun onResponse(
                call: Call<RandomKostResponse>,
                response: Response<RandomKostResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listRandomKos.value = response.body()?.randomkostitem
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RandomKostResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

}