package com.example.kostify.data.api.response

import com.google.gson.annotations.SerializedName

data class KostResponse(

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null
)

data class ItemsItem(

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
	val alamat: String? = null
)
