package com.example.mvvmfoodrecipesappproject.data.database

import androidx.room.*
import com.example.mvvmfoodrecipesappproject.data.database.entities.FavoritesEntity
import com.example.mvvmfoodrecipesappproject.data.database.entities.FoodJokeEntity
import com.example.mvvmfoodrecipesappproject.data.database.entities.RecipesEntity
import com.example.mvvmfoodrecipesappproject.model.FoodJoke
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity : RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Delete
    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY pid ASC")
    fun readRecipes() : Flow<List<RecipesEntity>>

    @Query("SELECT * FROM food_joke_entity ORDER By jokeId ASC")
    fun readFoodJoke() : Flow<List<FoodJokeEntity>>

    @Query("SELECT * FROM favorite_recipes_table ORDER BY mid ASC")
    fun readFavoriteRecipes() : Flow<List<FavoritesEntity>>

    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavoriteRecipes()

}