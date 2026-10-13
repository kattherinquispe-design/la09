package com.ejemplo.lab09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProgPrincipal9()
        }
    }
}

@Composable
fun ProgPrincipal9() {
    // API de Posts
    val urlBasePosts = "https://jsonplaceholder.typicode.com/"
    val retrofitPosts = Retrofit.Builder()
        .baseUrl(urlBasePosts)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val servicioPosts = retrofitPosts.create(PostApiService::class.java)

    // API de Recetas
    val urlBaseRecetas = "https://dummyjson.com/"
    val retrofitRecetas = Retrofit.Builder()
        .baseUrl(urlBaseRecetas)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val servicioRecetas = retrofitRecetas.create(RecipeApiService::class.java)

    // API de Anfibios (Google AppSpot URL Real)
    val urlBaseAnfibios = "https://android-kotlin-fun-mars-server.appspot.com/"
    val retrofitAnfibios = Retrofit.Builder()
        .baseUrl(urlBaseAnfibios)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val servicioAnfibios = retrofitAnfibios.create(AmphibianApiService::class.java)

    val navController = rememberNavController()

    Scaffold(
        topBar = { BarraSuperior() },
        bottomBar = { BarraInferior(navController) },
        content = { paddingValues ->
            Contenido(paddingValues, navController, servicioPosts, servicioRecetas, servicioAnfibios)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Lab 09 - Retrofit & APIs",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF4C5B93)
        )
    )
}

@Composable
fun BarraInferior(navController: NavHostController) {
    NavigationBar(
        containerColor = Color(0xFFEFEFEF)
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = navController.currentDestination?.route == "inicio",
            onClick = { navController.navigate("inicio") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Favorite, contentDescription = "Posts") },
            label = { Text("Posts") },
            selected = navController.currentDestination?.route == "posts",
            onClick = { navController.navigate("posts") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Restaurant, contentDescription = "Recetas") },
            label = { Text("Recetas") },
            selected = navController.currentDestination?.route == "recetas",
            onClick = { navController.navigate("recetas") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Pets, contentDescription = "Anfibios") },
            label = { Text("Anfibios") },
            selected = navController.currentDestination?.route == "anfibios",
            onClick = { navController.navigate("anfibios") }
        )
    }
}

@Composable
fun Contenido(
    pv: PaddingValues,
    navController: NavHostController,
    servicioPosts: PostApiService,
    servicioRecetas: RecipeApiService,
    servicioAnfibios: AmphibianApiService
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv)
    ) {
        NavHost(
            navController = navController,
            startDestination = "posts"
        ) {
            composable("inicio") { ScreenInicio() }
            composable("posts") { ScreenPosts(navController, servicioPosts) }
            composable(
                "postsVer/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) {
                val id = it.arguments?.getInt("id") ?: 1
                ScreenPost(navController, servicioPosts, id)
            }
            composable("recetas") { ScreenRecetas(navController, servicioRecetas) }
            composable(
                "recetasVer/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) {
                val id = it.arguments?.getInt("id") ?: 1
                ScreenReceta(navController, servicioRecetas, id)
            }
            composable("anfibios") { ScreenAnfibios(servicioAnfibios) }
        }
    }
}

@Composable
fun ScreenInicio() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("INICIO", modifier = Modifier.padding(16.dp))
    }
}