package com.example.kostify.data.api.response

import com.example.kostify.data.api.response.Bookmark.Bookmark
import com.google.gson.annotations.SerializedName

data class DetailKostResponse(

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("harga")
	val harga: Int? = null,

	@field:SerializedName("nama_kost")
	val namaKost: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("Luas kamar")
	val luasKamar: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("bookmarks") // Tambahkan properti baru untuk menyimpan daftar bookmark
	val bookmarks: List<Bookmark>? = null
)
