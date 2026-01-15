package com.example.museumapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
// CHANGE 1: Import Entity and ViewModel instead of DTO
import com.example.museumapp.database.ArtworkEntity
import com.example.museumapp.viewmodel.ArtworkViewModel

@Composable
// CHANGE 2: Add viewModel as a parameter
fun ArtworkDetailScreen(artworkId: String, viewModel: ArtworkViewModel) {

    // Use the ViewModel to find the specific artwork from the database list
    val artworkEntity = remember(artworkId) { viewModel.getArtworkById(artworkId) }

    if (artworkEntity == null) {
        ErrorState(message = "Artwork not found in database.")
    } else {
        ArtworkDetailContent(entity = artworkEntity)
    }
}

@Composable
// CHANGE 3: Accept ArtworkEntity instead of ArtworkDto
fun ArtworkDetailContent(entity: ArtworkEntity) {
    val scrollState = rememberScrollState()

    // Map the Entity fields directly to UI variables
    // Since we handled defaults in the Mapper, these Strings are safe to use
    val image = entity.imageUrl
    val title = entity.title
    val subtitle = entity.artist
    val description = entity.description
    val year = entity.year
    val type = entity.type
    val provider = entity.provider

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- 1. HERO IMAGE ---
        if (image.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image)
                    .crossfade(true)
                    .build(),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            )
        } else {
            Box(
                modifier = Modifier.fillMaxWidth().height(300.dp).background(Color.Gray),
                contentAlignment = Alignment.Center
            ) { Text("No Image", color = Color.White) }
        }

        // --- 2. TEXT CONTENT ---
        Column(modifier = Modifier.padding(16.dp)) {

            // Type Badge
            if (type.isNotEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = type,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.SemiBold
            )

            if (year.isNotEmpty()) {
                Text(
                    text = "Created in: $year",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

            Text(
                text = "About this work",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.4,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (provider.isNotEmpty()) {
                Text(
                    text = "Provided by: $provider",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ErrorState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error)
            Text(message)
        }
    }
}