package com.example.museumapp.repository

import android.util.Log
import com.example.museumapp.database.ArtworkDao
import com.example.museumapp.database.ArtworkEntity
import com.example.museumapp.database.toEntity
import com.example.museumapp.model.ArtworkDto
import com.example.museumapp.network.EuropeanaApi
import kotlinx.coroutines.flow.Flow


class ArtworkRepository(private val dao: ArtworkDao, private val api: EuropeanaApi) {
    private val apiKey = "ggslenste"

    val artworks: Flow<List<ArtworkEntity>> = dao.getAllArtworks()

    suspend fun getArtworks(query: String): List<ArtworkDto> {
        return try {
            val response = api.searchArtworks(apiKey, query)
            response.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun refreshArtworks(query: String) {
        try {
            val response = api.searchArtworks("ggslenste", query)
            val entities = response.items.map { it.toEntity() }
            dao.insertAll(entities)
        } catch (e: Exception) {
            // Network error: Do nothing, user sees cached data
            Log.e("Repository", "Error fetching data: ${e.message}")
        }
    }
}