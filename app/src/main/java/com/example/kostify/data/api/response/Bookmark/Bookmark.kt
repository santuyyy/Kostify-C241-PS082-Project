package com.example.kostify.data.api.response.Bookmark

data class Bookmark(
    val id: String?, // ID bisa null
    val createdAt: CreatedAt,
    val kostId: String,
    val kostDetails: KostDetails,
    val userId: String
)
