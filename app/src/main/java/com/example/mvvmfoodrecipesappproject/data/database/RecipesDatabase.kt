package com.example.mvvmfoodrecipesappproject.data.database

import androidx.room.*

@Database(
        entities = [RecipesEntity::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase  : RoomDatabase(){
    abstract fun recipesDao() : RecipesDao
}