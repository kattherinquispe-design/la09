package com.ejemplo.lab09

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ScreenRecetas(navController: NavHostController, servicio: RecipeApiService) {
    val listaRecetas: SnapshotStateList<RecipeModel> = remember { mutableStateListOf() }

    LaunchedEffect(Unit) {
        try {
            val response = servicio.getRecipes()
            response.recipes.forEach { listaRecetas.add(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LazyColumn {
        items(listaRecetas) { item ->
            Row(modifier = Modifier.padding(8.dp)) {
                Text(text = item.id.toString(), Modifier.weight(0.05f), textAlign = TextAlign.End)
                Spacer(Modifier.padding(horizontal = 1.dp))
                Text(text = item.name, Modifier.weight(0.7f))
                IconButton(
                    onClick = {
                        navController.navigate("recetasVer/${item.id}")
                        Log.e("RECETAS", "ID = ${item.id}")
                    },
                    modifier = Modifier.weight(0.1f)
                ) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = "Ver")
                }
            }
        }
    }
}

@Composable
fun ScreenReceta(navController: NavHostController, servicio: RecipeApiService, id: Int) {
    var receta by remember { mutableStateOf<RecipeModel?>(null) }

    LaunchedEffect(Unit) {
        try {
            receta = servicio.getRecipeById(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(
        Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        receta?.let { r ->
            OutlinedTextField(
                value = r.id.toString(),
                onValueChange = {},
                label = { Text("ID") },
                readOnly = true
            )
            OutlinedTextField(
                value = r.name,
                onValueChange = {},
                label = { Text("Nombre") },
                readOnly = true
            )
            OutlinedTextField(
                value = r.cuisine,
                onValueChange = {},
                label = { Text("Cocina") },
                readOnly = true
            )
            OutlinedTextField(
                value = r.difficulty,
                onValueChange = {},
                label = { Text("Dificultad") },
                readOnly = true
            )
        }
    }
}