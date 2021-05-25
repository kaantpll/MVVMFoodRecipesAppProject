package com.example.mvvmfoodrecipesappproject.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvvmfoodrecipesappproject.model.Result
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
data class FavoritesEntity(
        @PrimaryKey(autoGenerate = true)
        val mid : Int,
        var result : Result
)