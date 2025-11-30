package com.example.myzoodex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myzoodex.data.local.entity.AnimalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalDao {
    @Query("SELECT * FROM animals ORDER BY id ASC")
    fun getAnimals(): Flow<List<AnimalEntity>>

    @Query("SELECT * FROM animals WHERE id = :id")
    fun getAnimal(id: Int): Flow<AnimalEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimals(animals: List<AnimalEntity>)

    @Query("SELECT COUNT(*) FROM animals")
    suspend fun countAnimals(): Int
}
