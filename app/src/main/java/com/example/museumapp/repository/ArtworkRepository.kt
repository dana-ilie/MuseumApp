package com.example.museumapp.repository

import com.example.museumapp.model.ArtworkDto
import com.example.museumapp.network.RetrofitInstance

class ArtworkRepository {
    private val api = RetrofitInstance.api
    private val apiKey = "ggslenste"

    suspend fun getArtworks(query: String): List<ArtworkDto> {
        return try {
            val response = api.searchArtworks(apiKey, query)
            response.items
        } catch (e: Exception) {
            emptyList()
        }
    }
}
