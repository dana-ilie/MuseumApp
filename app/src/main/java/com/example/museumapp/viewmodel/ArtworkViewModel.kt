package com.example.museumapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.museumapp.database.ArtworkEntity
import com.example.museumapp.repository.ArtworkRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ArtworkViewModel(private val repository: ArtworkRepository) : ViewModel() {

    private val _dbArtworks = repository.artworks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedYear = MutableStateFlow<String?>(null)
    private val _selectedCountry = MutableStateFlow<String?>(null)

    val displayedArtworks: StateFlow<List<ArtworkEntity>> = combine(
        _dbArtworks,
        _selectedYear,
        _selectedCountry
    ) { artworks, year, country ->
        var filtered = artworks
        // Filter by Year if selected
        if (year != null) {
            filtered = filtered.filter { it.year == year }
        }
        // Filter by Country if selected
        if (country != null) {
            filtered = filtered.filter { it.country == country }
        }
        filtered
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableYears: StateFlow<List<String>> = _dbArtworks.map { list ->
        list.map { it.year }.filter { it.isNotBlank() }.distinct().sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableCountries: StateFlow<List<String>> = _dbArtworks.map { list ->
        list.map { it.country }.filter { it.isNotBlank() }.distinct().sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadArtworks("Van Gogh")
    }

    fun loadArtworks(query: String) {
        viewModelScope.launch {
            clearFilters() // Reset filters on new search
            repository.refreshArtworks(query)
        }
    }

    fun getArtworkById(id: String): ArtworkEntity? {
        return _dbArtworks.value.find { it.id == id }
    }

    // Filter Actions
    fun setYearFilter(year: String?) { _selectedYear.value = year }
    fun setCountryFilter(country: String?) { _selectedCountry.value = country }
    fun clearFilters() {
        _selectedYear.value = null
        _selectedCountry.value = null
    }
}