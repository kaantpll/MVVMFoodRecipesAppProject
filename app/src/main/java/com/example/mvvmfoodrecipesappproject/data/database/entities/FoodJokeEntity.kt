package com.example.mvvmfoodrecipesappproject.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvvmfoodrecipesappproject.model.FoodJoke
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.FOOD_JOKE_TABLE

@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity(
        @Embedded
        var foodJoke : FoodJoke
){
    @PrimaryKey(autoGenerate = false)
    var jokeId : Int = 0
}