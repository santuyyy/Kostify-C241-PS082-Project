package com.example.kostify.data.api.retrofit

import com.example.kostify.data.api.response.Bookmark.AddBookmarkRequest
import com.example.kostify.data.api.response.Bookmark.AddBookmarkResponse
import com.example.kostify.data.api.response.Bookmark.BookmarkResponse
import com.example.kostify.data.api.response.Bookmark.RemoveBookmarkRequest
import com.example.kostify.data.api.response.Bookmark.RemoveBookmarkResponse
import com.example.kostify.data.api.response.DetailKostResponse
import com.example.kostify.data.api.response.KostResponse
import com.example.kostify.data.api.response.RandomKostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("api/kost/search")
    fun getSearchKost(
        @Query("gender") gender: String,
    ): Call<KostResponse>

    @GET("api/kost/search")
    fun getRekomendasiKost(
        @Query("harga") harga: String,
        @Query("gender") gender: String,
        @Query("lokasi") lokasi: String,
        @Query("AC") ac: Int,
        @Query("Kasur") kasur: Int,
        @Query("Lemari") lemari: Int,
        @Query("Wifi") wifi: Int,
        @Query("Wc_duduk") wcDuduk: Int,
        @Query("Kamar_mandi_dalam") kamarMandiDalam: Int,
        @Query("Listrik") listrik: Int
    ): Call<KostResponse>


    @GET("api/kost/random-kos")
    fun getRandomKos(): Call<RandomKostResponse>

    @GET("api/kost/{id}")
    fun getDetailKost(
        @Path("id") id: Int
    ): Call<DetailKostResponse>

    @POST("api/bookmark/add")
    fun addBookmark(@Body addBookmarkRequest: AddBookmarkRequest): Call<AddBookmarkResponse>

    @HTTP(method = "DELETE", path = "api/bookmark/remove", hasBody = true)
    fun removeBookmark(@Body removeBookmarkRequest: RemoveBookmarkRequest): Call<RemoveBookmarkResponse>

    @GET("api/bookmark/{userId}")
    fun getBookmarks(@Path("userId") userId: String): Call<BookmarkResponse>
}