package com.example.kostify.data.api.response.Bookmark

data class KostDetails(
    val gender: String,
    val harga: Int,
    val nama_kost: String,
    val deskripsi: String,
    val id: Int,
    val luasKamar: String? = null,
    val alamat: String
)
