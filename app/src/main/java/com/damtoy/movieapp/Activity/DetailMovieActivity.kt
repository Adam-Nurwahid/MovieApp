package com.damtoy.movieapp.Activity

import android.os.Bundle
import androidx.activity.compose.setContent
// import androidx.activity.enableEdgeToEdge // Tidak digunakan secara langsung di sini, BaseActivity sudah menangani edge-to-edge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer // Mengganti Spancer menjadi Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items // Impor untuk items di LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // Impor untuk modifier .clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// import androidx.core.view.ViewCompat // Tidak digunakan secara langsung di sini
// import androidx.core.view.WindowInsetsCompat // Tidak digunakan secara langsung di sini
import coil.compose.AsyncImage
// import coil.compose.rememberImagePainter // Tidak digunakan, AsyncImage sudah cukup
import com.damtoy.movieapp.R
import com.damtoy.movieapp.domain.FilmItemModel

class DetailMovieActivity : BaseActivity() {
    private lateinit var filmItem: FilmItemModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filmItem = intent.getSerializableExtra("object") as? FilmItemModel ?: run {
            finish() // Menutup activity jika data tidak valid
            return // Menghentikan eksekusi onCreate lebih lanjut
        }

        setContent {
            DetailScreen(filmItem, onBackClick = { finish() })
        }
    }
}

@Composable
fun DetailScreen(film: FilmItemModel, onBackClick: () -> Unit) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.blackbackground))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier.height(400.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .padding(start = 16.dp, top = 48.dp)
                        .size(24.dp)
                        .clickable { onBackClick() }
                )
                Image(
                    painter = painterResource(R.drawable.fav),
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .padding(end = 16.dp, top = 48.dp)
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                )
                AsyncImage(
                    model = film.Poster,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.5f,
                )
                AsyncImage(
                    model = film.Poster,
                    contentDescription = film.Title,
                    modifier = Modifier
                        .size(210.dp, 300.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .align(Alignment.BottomCenter),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    colorResource(id = R.color.blackbackground).copy(alpha = 0.8f),
                                    colorResource(id = R.color.blackbackground) // Lebih pekat di bawah
                                )
                            )
                        )
                )
                Text(
                    text = film.Title,
                    style = androidx.compose.ui.text.TextStyle(
                        color = Color.White,
                        fontSize = 27.sp,
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(), // Agar row mengisi lebar
                    horizontalArrangement = Arrangement.SpaceAround, // Distribusi ruang yang lebih merata
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    InfoChip(iconRes = R.drawable.star, text = "IMDb ${film.Imdb}", )
                    InfoChip(iconRes = R.drawable.time, text = film.Time ?: "N/A") // Handle jika Time null
                    InfoChip(iconRes = R.drawable.cal, text = film.Year.toString())
                }

                Spacer(modifier = Modifier.height(24.dp)) // KOREKSI: Spancer -> Spacer
                Text(text = "Summary", style = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 18.sp /*fontWeight = FontWeight.Bold*/))
                Spacer(modifier = Modifier.height(8.dp)) // KOREKSI: Spancer -> Spacer
                Text(
                    text = film.Description,
                    style = androidx.compose.ui.text.TextStyle(
                        color = Color.LightGray, // Warna agar sedikit berbeda dari judul
                        fontSize = 14.sp,
                        lineHeight = 20.sp // Menambah jarak antar baris agar mudah dibaca
                    )
                )

                Spacer(modifier = Modifier.height(24.dp)) // KOREKSI: Spancer -> Spacer

                Text(text = "Actors", style = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 18.sp /*fontWeight = FontWeight.Bold*/))
                Spacer(modifier = Modifier.height(8.dp)) // KOREKSI: Spancer -> Spacer

                // LazyRow untuk nama aktor (jika masih ingin ditampilkan terpisah)
                // Jika nama aktor ingin digabung dengan gambar, bisa dimodifikasi
                if (film.Casts.any { !it.Actor.isNullOrEmpty() }) { // Hanya tampilkan jika ada nama aktor
                    LazyRow(contentPadding = PaddingValues(vertical = 4.dp)) {
                        items(film.Casts.filter { !it.Actor.isNullOrEmpty() }) { castMember ->
                            Text(
                                text = castMember.Actor!!, // Ambil nama aktor
                                color = Color.White,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }


                // LazyRow untuk gambar aktor
                if (film.Casts.any { !it.PicUrl.isNullOrEmpty() }) { // Hanya tampilkan jika ada URL gambar
                    LazyRow(contentPadding = PaddingValues(vertical = 4.dp)) {
                        items(film.Casts.filter { !it.PicUrl.isNullOrEmpty() }) { castMember ->
                            AsyncImage(
                                model = castMember.PicUrl,
                                contentDescription = castMember.Actor ?: "Cast member", // Deskripsi untuk gambar aktor
                                modifier = Modifier
                                    .size(80.dp, 120.dp) // Ukuran yang lebih proporsional untuk poster mini
                                    .padding(horizontal = 4.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp)) // Tambahan spacer di akhir konten scrollable
            }
        }
        // }
    }
}

// Composable kecil untuk menampilkan ikon dan teks (opsional, untuk merapikan bagian info)
@Composable
fun InfoChip(iconRes: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painterResource(iconRes),
            contentDescription = null, // Deskripsi bisa lebih spesifik jika perlu
            modifier = Modifier.size(18.dp), // Sesuaikan ukuran ikon
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = text, color = Color.White, fontSize = 12.sp)
    }
}