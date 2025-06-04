package com.damtoy.movieapp.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items // Pastikan ini diimpor
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FabPosition // Material 2
import androidx.compose.material.FloatingActionButton // Material 2
import androidx.compose.material.Icon // Material 2
import androidx.compose.material.Scaffold // Material 2
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue // Impor ini jika belum ada
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.damtoy.movieapp.BottomNavigationBar
import com.damtoy.movieapp.FilmItem
import com.damtoy.movieapp.R // Pastikan ini adalah paket R Anda yang benar
import com.damtoy.movieapp.SearchBar
import com.damtoy.movieapp.ViewModel.MainViewModel
import com.damtoy.movieapp.domain.FilmItemModel
import com.damtoy.movieapp.ui.theme.MovieAppTheme


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(onItemClick = {item ->
                val intent = Intent(this, DetailMovieActivity::class.java)
                intent.putExtra("object", item)
                startActivity(intent)
            })
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MovieAppTheme {
        MainScreen()
    }
}

@Composable
fun MainScreen(onItemClick: (FilmItemModel) -> Unit = {}) {

    Scaffold(
        bottomBar = { BottomNavigationBar() },
        floatingActionButton = {

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape) // Clip bentuk lingkaran untuk background
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                colorResource(id = R.color.pink),
                                colorResource(id = R.color.green)
                            )
                        )
                    )
                    .padding(3.dp), // Padding untuk menciptakan ketebalan border
                contentAlignment = Alignment.Center // Pusatkan FAB di dalam Box ini
            ) {
                // FloatingActionButton yang sebenarnya
                FloatingActionButton(
                    onClick = {
                        // TODO: Tambahkan aksi ketika FAB diklik
                    },
                    modifier = Modifier.size(58.dp), // Ukuran FAB sedikit lebih kecil dari Box luar
                    shape = CircleShape, // Pastikan FAB itu sendiri juga berbentuk lingkaran
                    backgroundColor = colorResource(id = R.color.black3), // Warna latar FAB (sesuai kode asli)
                    contentColor = Color.White // Warna ikon di dalam FAB
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.float_icon),
                        contentDescription = "Floating Action Button", // Deskripsi konten yang baik
                        modifier = Modifier.size(24.dp) // Sesuaikan ukuran ikon jika perlu
                    )
                }
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        backgroundColor = colorResource(id = R.color.blackbackground) // Background untuk Scaffold
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues) // Terapkan padding dari Scaffold
                .fillMaxSize() // Isi seluruh ruang yang tersedia
                .background(color = colorResource(id = R.color.blackbackground))
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            MainContent(onItemClick)
        }

    }
}

@Composable
fun MainContent(onItemClick: (FilmItemModel) -> Unit) {

    val viewModel = MainViewModel()
    val upcoming = remember { mutableStateListOf<FilmItemModel>() }
    val newMovies = remember { mutableStateListOf<FilmItemModel>() }

    var showUpcomingLoad by remember { mutableStateOf(true) } // ubah menjadi var
    var showNewMoviesLoading by remember { mutableStateOf(true) } // ubah menjadi var

    LaunchedEffect(Unit) {
        viewModel.loadUpcoming().observeForever {
            upcoming.clear()
            upcoming.addAll(it)
            showUpcomingLoad = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadItems().observeForever {
            newMovies.clear()
            newMovies.addAll(it)
            showNewMoviesLoading = false // KOREKSI DI SINI: showMoviesLoading -> showNewMoviesLoading
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 60.dp, bottom = 100.dp)
    ) {
        Text(
            text = "What would you like to watch?",
            style = TextStyle(
                color = Color.White,
                fontSize = 25.sp,
            ),
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        )
        SearchBar(hint = "Search Movie...")

        SectionTitle("New Movies")

        if (showNewMoviesLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth() // Ganti fillMaxHeight menjadi fillMaxWidth agar progress indicator terpusat dengan benar
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(newMovies) { item ->
                    FilmItem(item, onItemClick)
                }
            }
        }
        SectionTitle("Upcoming Movies ")
        if (showUpcomingLoad) {
            Box(
                modifier = Modifier
                    .fillMaxWidth() // Ganti fillMaxHeight menjadi fillMaxWidth
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(upcoming) { item ->
                    FilmItem(item, onItemClick)
                }
            }
        }
    }
}


@Composable
fun SectionTitle(title: String){
    Text(
        text= title,
        style = TextStyle(color = Color(0xFFBDBDBD), fontSize = 18.sp),
        modifier = Modifier.padding(start = 16.dp, top = 32.dp, bottom = 8.dp),
        fontWeight = FontWeight.Bold
    )
}