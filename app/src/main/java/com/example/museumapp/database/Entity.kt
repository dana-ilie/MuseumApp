package com.example.museumapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.museumapp.model.ArtworkDto

@Entity(tableName = "artworks")
data class ArtworkEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val imageUrl: String,
    val description: String,
    val year: String,
    val type: String,
    val provider: String,
    val country: String
)

fun ArtworkDto.toEntity(): ArtworkEntity {
    return ArtworkEntity(
        id = this.id,
        title = this.title?.firstOrNull() ?: "Untitled",
        artist = this.dcCreator?.firstOrNull() ?: "Unknown Artist",
        imageUrl = this.edmPreview?.firstOrNull() ?: "",

        description = this.dcDescription?.firstOrNull() ?: "No description available.",

        year = this.year?.firstOrNull() ?: "",

        type = this.type ?: "Unknown",

        provider = this.dataProvider?.firstOrNull() ?: "",
        country = this.country?.firstOrNull() ?: ""
    )
}