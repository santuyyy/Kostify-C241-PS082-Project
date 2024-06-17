package com.example.kostify.data.repository

import com.example.kostify.data.api.retrofit.ApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class DetailKosRepository @Inject constructor(
    private val apiService: ApiService,
    private val auth: FirebaseAuth = Firebase.auth
) {

    companion object {
        private const val TAG = "DetailKosRepository"
    }


}
