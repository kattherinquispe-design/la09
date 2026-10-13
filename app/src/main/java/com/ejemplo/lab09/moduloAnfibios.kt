package com.ejemplo.lab09

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ScreenAnfibios(servicio: AmphibianApiService) {
    val listaAnfibios: SnapshotStateList<AmphibianModel> = remember { mutableStateListOf() }

    LaunchedEffect(Unit) {
        try {
            val listado = servicio.getAmphibians()
            if (!listado.isNullOrEmpty()) {
                listaAnfibios.clear()
                listaAnfibios.addAll(listado)
            } else {
                cargarAnfibiosOficiales(listaAnfibios)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            cargarAnfibiosOficiales(listaAnfibios)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        items(listaAnfibios) { anfibio ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${anfibio.name} (${anfibio.type})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF1B5E20)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    AsyncImage(
                        model = anfibio.imgSrc,
                        contentDescription = anfibio.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = anfibio.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

private fun cargarAnfibiosOficiales(lista: SnapshotStateList<AmphibianModel>) {
    lista.clear()
    lista.addAll(
        listOf(
            AmphibianModel(
                name = "Great Basin Spadefoot",
                type = "Toad",
                description = "This toad spends most of its life underground due to the arid desert conditions in which it lives. Spadefoot toads earn the name because of their hind legs which are wedged to aid in digging. They are typically grey, green, or brown with dark spots.",
                imgSrc = "https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/great-basin-spadefoot.png"
            ),
            AmphibianModel(
                name = "Roraima Bush Toad",
                type = "Toad",
                description = "This toad is typically found in South America. Specifically on Mount Roraima at the boarders of Venezuala, Brazil, and Guyana, hence the name. The Roraiam Bush Toad is typically black with yellow spots or marbling along the throat and belly.",
                imgSrc = "https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/roraima-bush-toad.png"
            ),
            AmphibianModel(
                name = "Pacific Chorus Frog",
                type = "Frog",
                description = "Also known as the Pacific Treefrog, it is the most common frog on the Pacific Coast of North America. These frogs can vary in color between green and brown and can be identified by a brown stripe that runs from their nostril, through the eye, to the ear.",
                imgSrc = "https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/pacific-chorus-frog.png"
            ),
            AmphibianModel(
                name = "Blue Jeans Frog",
                type = "Frog",
                description = "Sometimes called the Strawberry Poison-Dart Frog, this little amphibian is identifiable by its bright red body and blueish-purple arms and legs. The Blue Jeans Frog is not toxic to humans like some of its close relatives, but it can be harmful to some predators.",
                imgSrc = "https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/blue-jeans-frog.png"
            ),
            AmphibianModel(
                name = "California Giant Salamander",
                type = "Salamander",
                description = "As the name implies, this salamander can be found in Northern California, as well as other parts of the Pacific Northwest. They prefer temperate areas with plenty of moisture and they typically can grow up to 30 cm.",
                imgSrc = "https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/california-giant-salamander.png"
            ),
            AmphibianModel(
                name = "Tiger Salamander",
                type = "Salamander",
                description = "Tiger Salamanders are typically found on the Atlantic Coast of North America. They are named for their coloring which is typically a brown body broken up by yellowish-greensish spots. While they like moist conditions, they don't spend very much time in bodies of water. Instead they prefer to burrow into loose soil.",
                imgSrc = "https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/tiger-salamander.png"
            )
        )
    )
}