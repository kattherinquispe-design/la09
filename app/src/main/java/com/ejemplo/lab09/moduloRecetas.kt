package com.ejemplo.lab09

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun ScreenRecetas(navController: NavHostController, servicio: RecipeApiService) {
    val listaRecetas: SnapshotStateList<RecipeModel> = remember { mutableStateListOf() }

    LaunchedEffect(Unit) {
        try {
            val response = servicio.getRecipes()
            if (response.recipes.isNotEmpty()) {
                listaRecetas.clear()
                listaRecetas.addAll(response.recipes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        items(listaRecetas) { item ->
            Card(
                onClick = { navController.navigate("recetasVer/${item.id}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = item.image,
                        contentDescription = item.name,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.name,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Cocina: ${item.cuisine} | Dificultad: ${item.difficulty}",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⭐ ${item.rating} / 5.0",
                                color = Color(0xFFE67E22),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "🔥 ${item.caloriesPerServing} kcal",
                                color = Color(0xFFE74C3C),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoBadge(title: String, value: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, fontSize = 11.sp, color = color, fontWeight = FontWeight.Bold)
            Text(text = value, fontSize = 13.sp, color = Color.DarkGray, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ScreenReceta(navController: NavHostController, servicio: RecipeApiService, id: Int) {
    var receta by remember { mutableStateOf<RecipeModel?>(null) }
    val scrollState = rememberScrollState()

    LaunchedEffect(id) {
        try {
            receta = servicio.getRecipeById(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        receta?.let { r ->
            // Imagen Principal con bordes redondeados
            AsyncImage(
                model = r.image,
                contentDescription = r.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Chips / Insignias de métricas rápidas (Tiempo, Calorías, Porciones, Dificultad)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoBadge("Tiempo Total", "${r.prepTimeMinutes + r.cookTimeMinutes} min", Color(0xFF2980B9))
                InfoBadge("Calorías", "${r.caloriesPerServing} kcal", Color(0xFFC0392B))
                InfoBadge("Porciones", "${r.servings} pers.", Color(0xFF27AE60))
                InfoBadge("Dificultad", r.difficulty, Color(0xFF8E44AD))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre de la Receta
            OutlinedTextField(
                value = r.name,
                onValueChange = {},
                label = { Text("Nombre de la Receta") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Tipo de Cocina y Valoración
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = r.cuisine,
                    onValueChange = {},
                    label = { Text("Tipo de Cocina") },
                    readOnly = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = "⭐ ${r.rating} / 5.0",
                    onValueChange = {},
                    label = { Text("Valoración") },
                    readOnly = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Ingredientes
            OutlinedTextField(
                value = r.ingredients.joinToString("\n• ", prefix = "• "),
                onValueChange = {},
                label = { Text("Ingredientes Necesarios") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Instrucciones paso a paso
            val instruccionesFormateadas = r.instructions.mapIndexed { index, paso ->
                "Paso ${index + 1}: $paso"
            }.joinToString("\n\n")

            OutlinedTextField(
                value = instruccionesFormateadas,
                onValueChange = {},
                label = { Text("Instrucciones de Preparación") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
        }
    }
}