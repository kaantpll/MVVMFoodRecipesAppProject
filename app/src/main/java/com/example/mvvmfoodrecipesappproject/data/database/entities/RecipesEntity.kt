package com.example.mvvmfoodrecipesappproject.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvvmfoodrecipesappproject.model.FoodRecipe
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
        var foodRecipes : FoodRecipe
){
    @PrimaryKey(autoGenerate = false)
    var pid : Int =0

}
