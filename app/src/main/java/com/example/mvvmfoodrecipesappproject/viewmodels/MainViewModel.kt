package com.example.mvvmfoodrecipesappproject.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.mvvmfoodrecipesappproject.data.Repository
import com.example.mvvmfoodrecipesappproject.data.database.RecipesEntity
import com.example.mvvmfoodrecipesappproject.model.FoodRecipe
import com.example.mvvmfoodrecipesappproject.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
        private val repository: Repository,
        application: Application) : AndroidViewModel(application)
{


        /**Room Database*/
        val readRecipes : LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()
        private fun insertRecipes(recipesEntity: RecipesEntity){
            viewModelScope.launch(Dispatchers.IO) {
                repository.local.insertRecipes(recipesEntity)
            }
        }




        /** Retrofit*/
        var recipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

        fun getRecipes(queries : Map<String,String>) = viewModelScope.launch {
            getRecipesSafeCall(queries)
        }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {

                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if(foodRecipe != null){
                    offlineCacheRecipes(foodRecipe)
                }
            }
            catch (e : Exception){
                recipesResponse.value = NetworkResult.Error("No Internet Connection.")
            }
        }
        else{
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when{
            response.message().toString().contains("timeout")->{
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 ->{
                return NetworkResult.Error("API key Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful->{
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else ->{
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean{
             val connectActivityManager = getApplication<Application>().getSystemService(
                     Context.CONNECTIVITY_SERVICE
             ) as ConnectivityManager

             val activeNetwork = connectActivityManager.activeNetwork ?: return false
             val capabilities = connectActivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
         }
}