package com.example.mvvmfoodrecipesappproject.viewmodels

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmfoodrecipesappproject.data.DataStoreRepository
import com.example.mvvmfoodrecipesappproject.util.Constants
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.QUERY_API_KEY
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.QUERY_DIET
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.QUERY_NUMBER
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.QUERY_TYPE
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.RECIPES_TABLE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecipesViewModel @ViewModelInject
constructor(application: Application,
            private val dataStoreRepository : DataStoreRepository)
    : AndroidViewModel(application) {

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    fun saveMealAndDietType(mealType : String,mealTypeId : Int , dietType : String,dietTypeId : Int){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType,mealTypeId,dietType,dietTypeId)
        }
    }

     fun applyQueries(): HashMap<String,String>{
        val queries : HashMap<String,String> = HashMap()

         viewModelScope.launch {
             readMealAndDietType.collect { value ->

               mealType = value.selectedMealType
               dietType = value.selectedDietType

             }
         }

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = Constants.API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] =   dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }
}