package com.example.museumapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.museumapp.ui.theme.MuseumAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: ArtworkViewModel = viewModel() // Use simpler viewModel() factory or Hilt

            NavHost(navController = navController, startDestination = "list") {
                composable("list") {
                    ArtworkListScreen(viewModel) { id ->
                        // Encode the ID to handle slashes
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
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MuseumAppTheme {
        Greeting("Android")
    }
}