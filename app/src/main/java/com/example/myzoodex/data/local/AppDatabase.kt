package com.example.myzoodex.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myzoodex.data.local.dao.AnimalDao
import com.example.myzoodex.data.local.entity.AnimalEntity
import com.example.myzoodex.data.repository.AnimalSeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [AnimalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun animalDao(): AnimalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun buildDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "myzoodex.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            scope.launch(Dispatchers.IO) {
                                INSTANCE?.animalDao()?.insertAnimals(
                                    AnimalSeedData.animals.map { AnimalEntity.fromModel(it) }
                                )
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
