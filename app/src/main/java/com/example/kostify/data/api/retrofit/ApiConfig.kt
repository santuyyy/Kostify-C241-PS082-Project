package com.example.kostify.data.api.retrofit

import com.example.kostify.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        fun getApiService(): ApiService {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
         /*   val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "AIzaSyDUBmzr8sXEFF6_gzEIjTeoJWSOymYqUOc")
                    .build()
                chain.proceed(requestHeaders)
            }*/
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                /*.addInterceptor(authInterceptor)*/
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://kostify-project.et.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}