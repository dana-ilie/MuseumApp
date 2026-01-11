package com.example.museumapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.museumapp.database.ArtworkEntity
import com.example.museumapp.viewmodel.ArtworkViewModel

@Composable
fun ArtworkListScreen(
    viewModel: ArtworkViewModel,
    onArtworkClick: (String) -> Unit
) {
    val artworks by viewModel.artworks.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Europeana Museum", style = MaterialTheme.typography.headlineMedium)

        LazyColumn {
            items(artworks) { artwork ->
                ArtworkRow(artwork, onArtworkClick)
            }
        }
    }
}

@Composable
fun ArtworkRow(artwork: ArtworkEntity, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(artwork.id) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Image Loading
            AsyncImage(
                model = artwork.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = artwork.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = artwork.artist,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}