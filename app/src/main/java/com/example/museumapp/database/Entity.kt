package com.example.museumapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.museumapp.model.ArtworkDto

@Entity(tableName = "artworks")
data class ArtworkEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val imageUrl: String
)

// Extension function to map DTO to Entity
fun ArtworkDto.toEntity(): ArtworkEntity {
    return ArtworkEntity(
        id = this.id,
        title = this.title?.firstOrNull() ?: "Unknown",
        artist = this.dcCreator?.firstOrNull() ?: "Unknown",
        imageUrl = this.edmPreview?.firstOrNull() ?: ""
    )
}