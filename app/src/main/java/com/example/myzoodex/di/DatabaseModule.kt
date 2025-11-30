package com.example.myzoodex.di

import android.content.Context
import com.example.myzoodex.data.local.AppDatabase
import com.example.myzoodex.data.repository.AnimalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object DatabaseModule {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Volatile
    private var database: AppDatabase? = null

    @Volatile
    private var animalRepository: AnimalRepository? = null

    fun provideDatabase(context: Context): AppDatabase =
        database ?: synchronized(this) {
            database ?: AppDatabase.buildDatabase(context, applicationScope).also { database = it }
        }

    fun provideAnimalRepository(context: Context): AnimalRepository =
        animalRepository ?: synchronized(this) {
            animalRepository ?: AnimalRepository(provideDatabase(context).animalDao()).also {
                animalRepository = it
            }
        }
}
