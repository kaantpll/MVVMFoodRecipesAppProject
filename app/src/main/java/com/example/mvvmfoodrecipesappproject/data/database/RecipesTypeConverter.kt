package com.example.mvvmfoodrecipesappproject.data.database

import androidx.room.TypeConverter
import com.example.mvvmfoodrecipesappproject.model.FoodRecipe
import com.example.mvvmfoodrecipesappproject.model.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipesToString(foodRecipe: FoodRecipe) : String{
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data : String) : FoodRecipe{
        val listType = object : TypeToken<FoodRecipe>(){}.type
        return gson.fromJson(data,listType)
    }

    @TypeConverter
    fun resultToString(result : Result): String{
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data : String) : Result{
        val listType = object : TypeToken<Result>(){}.type
        return gson.fromJson(data,listType)
    }
}