package com.ejemplo.lab09

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ScreenPosts(navController: NavHostController, servicio: PostApiService) {
    val listaPosts: SnapshotStateList<PostModel> = remember { mutableStateListOf() }

    LaunchedEffect(Unit) {
        try {
            val listado = servicio.getUserPosts()
            if (!listado.isNullOrEmpty()) {
                listaPosts.clear()
                listado.forEach { listaPosts.add(it) }
            } else {
                cargarFallbackPosts(listaPosts)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            cargarFallbackPosts(listaPosts)
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 4.dp)) {
        items(listaPosts) { item ->
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.id.toString(),
                    modifier = Modifier.weight(0.08f),
                    textAlign = TextAlign.Start
                )
                Spacer(Modifier.padding(horizontal = 2.dp))
                Text(
                    text = item.title,
                    modifier = Modifier.weight(0.78f)
                )
                IconButton(
                    onClick = {
                        navController.navigate("postsVer/${item.id}")
                        Log.e("POSTS", "ID = ${item.id}")
                    },
                    modifier = Modifier.weight(0.14f)
                ) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = "Ver")
                }
            }
        }
    }
}

private fun cargarFallbackPosts(lista: SnapshotStateList<PostModel>) {
    lista.clear()
    lista.addAll(
        listOf(
            PostModel(1, 1, "sunt aut facere repellat provident occaecati excepturi optio reprehenderit", "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"),
            PostModel(1, 2, "qui est esse", "est rerum tempore vitae\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\nqui aperiam non debitis possimus qui neque nisi nulla"),
            PostModel(1, 3, "ea molestias quasi exercitationem repellat qui ipsa sit aut", "et iusto sed quo iure\nvoluptatem occaecati omnis aliquid aut auge\narchitecto"),
            PostModel(1, 4, "eum et est occaecati", "ullam et saepe reiciendis voluptatem adipisci\nsit amet autem assumenda provident rerum culpa\nquis hic commodi nesciunt rem tenetur dolores ipsam iure\nquis sunt voluptatem rerum illo velit"),
            PostModel(1, 5, "nesciunt quas odio", "repudiandae veniam quaerat sunt sed\nalias aut fugiat sit autem sed est\nvoluptatem omnis possimus esse voluptatibus quis\nest aut tenet"),
            PostModel(1, 6, "dolorem eum magni eos aporiam quia", "ut aspernatur corporis harum nihil quis provident sequi\nmollitia nobis aliquid molestiae\nperspiciatis et ea nemo ab reprehenderit accusantium quas\nvoluptate dolores velit et doloremque molestiae")
        )
    )
}

@Composable
fun ScreenPost(navController: NavHostController, servicio: PostApiService, id: Int) {
    var post by remember { mutableStateOf<PostModel?>(null) }

    LaunchedEffect(id) {
        try {
            post = servicio.getUserPostById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            // Si falla la red, busca en el listado de fallback
            val fallbackList = mutableStateListOf<PostModel>()
            cargarFallbackPosts(fallbackList)
            post = fallbackList.find { it.id == id } ?: PostModel(1, id, "Post $id", "Contenido del post $id")
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        post?.let { p ->
            OutlinedTextField(
                value = p.id.toString(),
                onValueChange = {},
                label = { Text("id") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = p.userId.toString(),
                onValueChange = {},
                label = { Text("userId") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = p.title,
                onValueChange = {},
                label = { Text("title") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = p.body,
                onValueChange = {},
                label = { Text("body") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }
    }
}