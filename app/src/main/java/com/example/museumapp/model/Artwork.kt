package com.example.museumapp.model

data class EuropeanaResponse(
    val items: List<ArtworkDto>
)

data class ArtworkDto(
    val id: String,
    val title: List<String>?,
    val type: String?,
    val edmPreview: List<String>?,      // Image URL
    val dcDescription: List<String>?,   // Description text
    val dcCreator: List<String>?,       // Artist/Author
    val dataProvider: List<String>?,    // Museum/Provider
    val year: List<String>?             // Creation year
)