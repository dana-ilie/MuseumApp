package com.example.museumapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.museumapp.repository.ArtworkRepository

class ArtworkViewModelFactory(private val repository: ArtworkRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtworkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArtworkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}