package com.example.mvvmfoodrecipesappproject.data.database

import androidx.room.*
import com.example.mvvmfoodrecipesappproject.data.database.entities.FavoritesEntity
import com.example.mvvmfoodrecipesappproject.data.database.entities.FoodJokeEntity
import com.example.mvvmfoodrecipesappproject.data.database.entities.RecipesEntity

@Database(
        entities = [RecipesEntity::class,FavoritesEntity::class,FoodJokeEntity::class],
        version = 2,
        exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase  : RoomDatabase(){
    abstract fun recipesDao() : RecipesDao
}