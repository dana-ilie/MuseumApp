package com.example.museumapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.museumapp.database.AppDatabase
import com.example.museumapp.network.RetrofitInstance
import com.example.museumapp.repository.ArtworkRepository
import com.example.museumapp.ui.screens.ArtworkDetailScreen
import com.example.museumapp.ui.screens.ArtworkListScreen
import com.example.museumapp.ui.theme.MuseumAppTheme
import com.example.museumapp.viewmodel.ArtworkViewModel
import com.example.museumapp.viewmodel.ArtworkViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the Room Database
        // "museum-db" = the name of the file saved on the device
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "museum-db"
        ).build()

        // Initialize the Repository
        // It needs the DAO (from DB) and the API (from Retrofit)
        val repository = ArtworkRepository(db.artworkDao(), RetrofitInstance.api)

        // Initialize the ViewModel Factory
        // This factory knows how to pass the repository to the ViewModel
        val viewModelFactory = ArtworkViewModelFactory(repository)

        setContent {
            MuseumAppTheme {
                val navController = rememberNavController()

                val viewModel: ArtworkViewModel = viewModel(factory = viewModelFactory)

                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        ArtworkListScreen(viewModel) { id ->
                            // Encode the ID to handle slashes in Europeana IDs
                            val encodedId = java.net.URLEncoder.encode(id, "UTF-8")
                            navController.navigate("details/$encodedId")
                        }
                    }
                    composable(
                        "details/{artworkId}",
                        arguments = listOf(navArgument("artworkId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val artworkId = backStackEntry.arguments?.getString("artworkId") ?: ""
                        ArtworkDetailScreen(artworkId)
                    }
                }
            }
        }
    }
}