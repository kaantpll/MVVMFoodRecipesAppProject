package com.example.mvvmfoodrecipesappproject.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context : Context){

    private object PreferencesKeys{
        val selectedMealType = preferencesKey<String>(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = preferencesKey<Int>(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = preferencesKey<String>(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = preferencesKey<Int>(PREFERENCES_DIET_TYPE_ID)
        val backOnline = preferencesKey<Boolean>(PREFERENCES_BACK_ONLINE)
    }

    private val dataStore : DataStore<Preferences> = context.createDataStore(
        name = PREFERENCES_NAME

    )

    suspend fun saveMealAndDietType(mealType : String,mealTypeId:Int,dietType : String,dietTypeId : Int){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.selectedMealType] = mealType
            preferences[PreferencesKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferencesKeys.selectedDietType] = dietType
            preferences[PreferencesKeys.selectedDietTypeId] = dietTypeId

        }
    }


    suspend fun saveBackOnline(backOnline : Boolean){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.backOnline] = backOnline
        }
    }

    val readMealAndDietType : Flow<MealAndDietType> = dataStore.data
        .catch {e->
            if(e is IOException){
                emit(emptyPreferences())
            }else{
                throw e
            }
        }.map { preferences->
            val selectedMealType = preferences[PreferencesKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferencesKeys.selectedMealTypeId] ?: 0
            val selectedDietType= preferences[PreferencesKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferencesKeys.selectedDietTypeId] ?: 0

            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )

        }

    val readBackOnline : Flow<Boolean> = dataStore.data.catch { e->
        if(e is IOException){
            emit(emptyPreferences())
        }else{
            throw e
        }
    }.map { prefence->
        val backOnline = prefence[PreferencesKeys.backOnline] ?: false
        backOnline
    }
}

data class MealAndDietType(
    val selectedMealType : String,
    val selectedMealTypeId : Int,
    val selectedDietType : String,
    val selectedDietTypeId :Int
)