package com.example.mvvmfoodrecipesappproject.data

import com.example.mvvmfoodrecipesappproject.data.database.RecipesDao
import com.example.mvvmfoodrecipesappproject.data.database.entities.FavoritesEntity
import com.example.mvvmfoodrecipesappproject.data.database.entities.FoodJokeEntity
import com.example.mvvmfoodrecipesappproject.data.database.entities.RecipesEntity
import com.example.mvvmfoodrecipesappproject.model.FoodJoke
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
        private val recipesDao : RecipesDao
)
{

    fun readRecipes() : Flow<List<RecipesEntity>>{
        return recipesDao.readRecipes()
    }

     fun readFavoriteRecipes() : Flow<List<FavoritesEntity>>{
         return recipesDao.readFavoriteRecipes()
     }

    fun readFoodJoke() : Flow<List<FoodJoke>>{
        return recipesDao.readFoodJoke()
    }


   suspend fun insertRecipes(recipesEntity: RecipesEntity){
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity){
        recipesDao.insertFavoriteRecipe(favoritesEntity)
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        recipesDao.insertFoodJoke(foodJokeEntity)
    }

    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity){
        recipesDao.deleteFavoriteRecipe(favoritesEntity)
    }

    suspend fun deleteAllFavoriteRecipes(){
        recipesDao.deleteAllFavoriteRecipes()
    }
}