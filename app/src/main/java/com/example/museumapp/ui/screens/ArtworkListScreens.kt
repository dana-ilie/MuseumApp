package com.example.museumapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.museumapp.database.ArtworkEntity
import com.example.museumapp.viewmodel.ArtworkViewModel

@Composable
fun ArtworkListScreen(
    viewModel: ArtworkViewModel,
    onArtworkClick: (String) -> Unit
) {
    val artworks by viewModel.displayedArtworks.collectAsState()

    val availableYears by viewModel.availableYears.collectAsState()
    val availableCountries by viewModel.availableCountries.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Europeana Museum", style = MaterialTheme.typography.headlineMedium)

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Artworks") },
                modifier = Modifier.weight(1f),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    val queryToSend = searchQuery.ifBlank { "*" }
                    viewModel.loadArtworks(queryToSend)
                })
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Filter Button
            FilledTonalIconButton(
                onClick = { showFilterDialog = true },
                modifier = Modifier.size(56.dp) // Match height of TextField
            ) {
                Icon(Icons.Default.FilterList, contentDescription = "Filter")
            }
        }

        // List Content
        if (artworks.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No artworks found matching filters.")
            }
        } else {
            LazyColumn {
                items(artworks) { artwork ->
                    ArtworkRow(artwork, onArtworkClick)
                }
            }
        }
    }

    // Show Dialog if button clicked
    if (showFilterDialog) {
        FilterDialog(
            years = availableYears,
            countries = availableCountries,
            onApply = { year, country ->
                viewModel.setYearFilter(year)
                viewModel.setCountryFilter(country)
                showFilterDialog = false
            },
            onClear = {
                viewModel.clearFilters()
                showFilterDialog = false
            },
            onDismiss = { showFilterDialog = false }
        )
    }
}

@Composable
fun FilterDialog(
    years: List<String>,
    countries: List<String>,
    onApply: (String?, String?) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedYear by remember { mutableStateOf<String?>(null) }
    var selectedCountry by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Results") },
        text = {
            Column {
                Text("Year", style = MaterialTheme.typography.labelLarge)
                FilterDropdown(options = years, selected = selectedYear) { selectedYear = it }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Country", style = MaterialTheme.typography.labelLarge)
                FilterDropdown(options = countries, selected = selectedCountry) { selectedCountry = it }
            }
        },
        confirmButton = {
            Button(onClick = { onApply(selectedYear, selectedCountry) }) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onClear) { Text("Clear All") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdown(options: List<String>, selected: String?, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selected ?: "All",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
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
            AsyncImage(
                model = artwork.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = artwork.title, style = MaterialTheme.typography.titleMedium)
                Text(text = artwork.artist, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "${artwork.year} • ${artwork.country}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}