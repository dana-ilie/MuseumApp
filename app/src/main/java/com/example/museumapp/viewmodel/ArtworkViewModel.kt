package com.example.museumapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.museumapp.model.ArtworkDto
import com.example.museumapp.repository.ArtworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArtworkViewModel : ViewModel() {
    private val repository = ArtworkRepository()

    // State flow for UI to observe
    private val _artworks = MutableStateFlow<List<ArtworkDto>>(emptyList())
    val artworks: StateFlow<List<ArtworkDto>> = _artworks

    init {
        loadArtworks("Leonardo da Vinci") // Default search
    }

    fun loadArtworks(query: String) {
        viewModelScope.launch {
            val result = repository.getArtworks(query)
            _artworks.value = result
        }
    }
}