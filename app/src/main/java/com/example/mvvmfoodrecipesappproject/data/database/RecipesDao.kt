package com.example.mvvmfoodrecipesappproject.data.database

import androidx.room.*
import com.example.mvvmfoodrecipesappproject.data.database.entities.FavoritesEntity
import com.example.mvvmfoodrecipesappproject.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity : RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity)


    @Delete
    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY pid ASC")
    fun readRecipes() : Flow<List<RecipesEntity>>


    @Query("SELECT * FROM favorite_recipes_table ORDER BY mid ASC")
    fun readFavoriteRecipes() : Flow<List<FavoritesEntity>>

    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavoriteRecipes()

}