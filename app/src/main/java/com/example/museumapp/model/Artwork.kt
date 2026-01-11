package com.example.museumapp.model

data class EuropeanaResponse(
    val items: List<ArtworkDto>
)

data class ArtworkDto(
    val id: String,
    val title: List<String>?,
    val dcCreator: List<String>?,
    val edmPreview: List<String>?,
    val type: String?
)