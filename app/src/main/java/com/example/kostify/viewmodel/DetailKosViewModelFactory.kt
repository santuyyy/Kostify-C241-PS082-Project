/*
package com.example.kostify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kostify.data.repository.DetailKosRepository

class DetailKosViewModelFactory(private val repository: DetailKosRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailKosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailKosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/
