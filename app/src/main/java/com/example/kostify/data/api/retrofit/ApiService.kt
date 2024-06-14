package com.example.kostify.data.api.retrofit

import com.example.kostify.data.api.response.DetailKostResponse
import com.example.kostify.data.api.response.KostResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("api/kost/search")
    fun getSearchKost(
        @Query("gender") gender: String,
    ): Call<KostResponse>

    @GET("api/kost/{id}")
    fun getDetailKost(
        @Path("id") id: Int
    ): Call<DetailKostResponse>


/*    @GET("api/kost/random-kos")
    fun getRandomKos(

    )*/
}