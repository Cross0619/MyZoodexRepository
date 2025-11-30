package com.example.myzoodex.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myzoodex.model.Animal

@Entity(tableName = "animals")
data class AnimalEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val distribution: String,
    val terrain: String,
    val area: String,
    val count: Int,
    val popularity: Int,
    @ColumnInfo(name = "animal_order")
    val orderName: String,
    val family: String,
    val food: String,
    val size: String,
    @ColumnInfo(name = "detailed_description")
    val detailedDescription: String,
    @ColumnInfo(name = "image_res")
    val imageRes: Int
) {
    fun toModel(): Animal = Animal(
        id = id,
        name = name,
        distribution = distribution,
        terrain = terrain,
        area = area,
        count = count,
        popularity = popularity,
        order = orderName,
        family = family,
        food = food,
        size = size,
        detailedDescription = detailedDescription,
        imageRes = imageRes
    )

    companion object {
        fun fromModel(animal: Animal): AnimalEntity = AnimalEntity(
            id = animal.id,
            name = animal.name,
            distribution = animal.distribution,
            terrain = animal.terrain,
            area = animal.area,
            count = animal.count,
            popularity = animal.popularity,
            orderName = animal.order,
            family = animal.family,
            food = animal.food,
            size = animal.size,
            detailedDescription = animal.detailedDescription,
            imageRes = animal.imageRes
        )
    }
}
