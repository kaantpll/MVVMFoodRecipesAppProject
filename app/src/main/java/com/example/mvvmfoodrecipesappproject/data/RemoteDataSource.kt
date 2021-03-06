package com.example.mvvmfoodrecipesappproject.data

import com.example.mvvmfoodrecipesappproject.data.network.FoodRecipesApi
import com.example.mvvmfoodrecipesappproject.model.FoodJoke
import com.example.mvvmfoodrecipesappproject.model.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi : FoodRecipesApi
)
{
    suspend fun getRecipes(queries : Map<String,String>) : Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery : Map<String,String>) : Response<FoodRecipe>{
        return foodRecipesApi.searchRecipes(searchQuery)
    }

    suspend fun getFoodJoke(apiKey:String) : Response<FoodJoke>{
        return foodRecipesApi.getFoodJoke(apiKey)
    }

}