package com.example.myzoodex.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myzoodex.data.repository.AnimalRepository
import com.example.myzoodex.data.repository.AnimalSeedData
import com.example.myzoodex.di.DatabaseModule
import com.example.myzoodex.model.Animal
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnimalViewModel(private val repository: AnimalRepository) : ViewModel() {

    val animals: StateFlow<List<Animal>> = repository
        .getAnimals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            repository.seedIfEmpty(AnimalSeedData.animals)
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val repository = DatabaseModule.provideAnimalRepository(application)
                    return AnimalViewModel(repository) as T
                }
            }
    }
}
