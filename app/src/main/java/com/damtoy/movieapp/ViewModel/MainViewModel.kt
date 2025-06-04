package com.damtoy.movieapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.damtoy.movieapp.Repository.MainRepository
import com.damtoy.movieapp.domain.FilmItemModel

class MainViewModel:ViewModel()  {
private val repository=MainRepository()
    fun loadUpcoming(): LiveData<MutableList<FilmItemModel>> {
    return repository.loadUpcoming()
    }

    fun loadItems(): LiveData<MutableList<FilmItemModel>> {
        return repository.loadItems()
    }
}