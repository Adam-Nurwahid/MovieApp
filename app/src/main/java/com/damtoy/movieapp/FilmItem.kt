package com.damtoy.movieapp

import android.util.Log // Import Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // Untuk ImageLoader
import androidx.compose.ui.text.TextStyle
// import androidx.compose.ui.text.font.FontWeight // Opsional
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest // Untuk listener
import com.damtoy.movieapp.domain.FilmItemModel

@Composable
fun FilmItem(item: FilmItemModel, onItemClick: (FilmItemModel) -> Unit) {
    // Log untuk memeriksa URL yang diterima
    Log.d("FilmItemDebug", "Loading item: ${item.Title}, Poster URL: ${item.Poster}")

    val imageError = remember { mutableStateOf(false) } // State untuk menandai error gambar

    Column(
        modifier = Modifier
            .width(120.dp)
            .background(color = Color(0xFF2f2f39)) // Cara standar set color dari hex
            .clickable { onItemClick(item) }
            .padding(4.dp)
    ) {
        if (imageError.value || item.Poster.isBlank()) {
            // Tampilkan placeholder jika URL kosong atau terjadi error
            Column(
                modifier = Modifier
                    .size(width = 120.dp, height = 180.dp)
                    .background(Color.DarkGray),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Image Load Error",
                    tint = Color.LightGray,
                    modifier = Modifier.size(48.dp).padding(top = 60.dp)
                )
                Text("No Image", color = Color.LightGray, fontSize = 10.sp)
            }
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.Poster)
                    .crossfade(true)
                    .listener(
                        onStart = { request ->
                            Log.d("CoilDebug", "Image loading started for: ${request.data}")
                            imageError.value = false // Reset error state on new load attempt
                        },
                        onSuccess = { request, result ->
                            Log.d("CoilDebug", "Image loaded successfully: ${request.data}")
                            imageError.value = false
                        },
                        onError = { request, result ->
                            Log.e("CoilError", "Error loading image: ${request.data}", result.throwable)
                            imageError.value = true // Set error state
                        }
                    )
                    .build(),
                contentDescription = item.Title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 120.dp, height = 180.dp)
                    .background(Color.Gray) // Background sementara saat loading
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.Title,
            modifier = Modifier.padding(horizontal = 4.dp),
            style = TextStyle(
                color = Color.White,
                fontSize = 11.sp,
            ),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Rating",
                tint = Color(0xFFFFC107) // Kuning untuk bintang
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = item.Imdb.toString(), // Pastikan Imbd adalah rating yang valid
                style = TextStyle(color = Color.White, fontSize = 12.sp)
            )
        }
    }
}