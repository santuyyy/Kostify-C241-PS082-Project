package com.example.kostify.data.api.response

import com.google.gson.annotations.SerializedName

data class BookmarkResponse(

	@field:SerializedName("BookmarkResponse")
	val bookmarkResponse: List<BookmarkResponseItem>
)

data class CreatedAt(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int,

	@field:SerializedName("_seconds")
	val seconds: Int
)

data class KostDetails(

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("nama_kost")
	val namaKost: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("Luas kamar")
	val luasKamar: String,

	@field:SerializedName("alamat")
	val alamat: String
)

data class BookmarkResponseItem(

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt,

	@field:SerializedName("kostId")
	val kostId: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("kostDetails")
	val kostDetails: KostDetails,

	@field:SerializedName("userId")
	val userId: String
)
