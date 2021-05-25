package com.example.mvvmfoodrecipesappproject.data.database

import androidx.room.*
import com.example.mvvmfoodrecipesappproject.data.database.entities.FavoritesEntity
import com.example.mvvmfoodrecipesappproject.data.database.entities.RecipesEntity

@Database(
        entities = [RecipesEntity::class,FavoritesEntity::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase  : RoomDatabase(){
    abstract fun recipesDao() : RecipesDao
}