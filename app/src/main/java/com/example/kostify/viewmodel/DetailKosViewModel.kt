package com.example.kostify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kostify.data.api.response.DetailKostResponse
import com.example.kostify.data.api.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailKosViewModel : ViewModel() {
    private val _detailKos = MutableLiveData<DetailKostResponse>()
    val detailKos : LiveData<DetailKostResponse> = _detailKos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
       private const val TAG = "DetailKosViewModel"
    }

    fun getDetailKos(id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailKost(id)

        client.enqueue(object : Callback<DetailKostResponse> {
            override fun onResponse(
                call: Call<DetailKostResponse>,
                response: Response<DetailKostResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailKos.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailKostResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}