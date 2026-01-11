package com.example.museumapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.museumapp.database.ArtworkEntity
import com.example.museumapp.repository.ArtworkRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ArtworkViewModel(private val repository: ArtworkRepository) : ViewModel() {

    val artworks: StateFlow<List<ArtworkEntity>> = repository.artworks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadArtworks("Leonardo da Vinci")
    }

    fun loadArtworks(query: String) {
        viewModelScope.launch {
            repository.refreshArtworks(query)
        }
    }
}