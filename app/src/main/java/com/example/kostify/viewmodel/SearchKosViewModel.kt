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

    private val _listRekomendasiKos = MutableLiveData<List<ItemsItem?>?>()
    val listRekomendasiKos : LiveData<List<ItemsItem?>?> = _listRekomendasiKos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "SearchKosViewModel"
    }

    fun findKostSearch(gender: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchKost(gender)

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

    fun getRecomendasiKost(harga: String, gender: String, lokasi: String, fasilitas: Map<String, Int>) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getRekomendasiKost(harga,
            gender,
            lokasi,
            fasilitas["AC"] ?: 0,
            fasilitas["Kasur"] ?: 0,
            fasilitas["Lemari"] ?: 0,
            fasilitas["Wifi"] ?: 0,
            fasilitas["Wc_duduk"] ?: 0,
            fasilitas["Kamar_mandi_dalam"] ?: 0,
            fasilitas["Listrik"] ?: 0,)

        client.enqueue(object : Callback<KostResponse> {
            override fun onResponse(
                call: Call<KostResponse>,
                response: Response<KostResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listRekomendasiKos.value = response.body()?.items

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