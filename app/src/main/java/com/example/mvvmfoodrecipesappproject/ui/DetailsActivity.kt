package com.example.mvvmfoodrecipesappproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.mvvmfoodrecipesappproject.R
import com.example.mvvmfoodrecipesappproject.adapters.PagerAdapter
import com.example.mvvmfoodrecipesappproject.data.database.entities.FavoritesEntity
import com.example.mvvmfoodrecipesappproject.ui.fragments.ingredients.IngredientsFragment
import com.example.mvvmfoodrecipesappproject.ui.fragments.instructions.InstructionsFragment
import com.example.mvvmfoodrecipesappproject.ui.fragments.overview.OverviewFragment
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.RECIPE_RESULT_KEY
import com.example.mvvmfoodrecipesappproject.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*
import java.lang.Exception

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel : MainViewModel by viewModels()
    private var recipesSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY,args.result)

        val adapter = PagerAdapter(
            resultBundle,fragments,titles,supportFragmentManager
        )

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu,menu)
        val menuItem = menu?.findItem(R.id.save_to_favorites_menu)
        checkSavedRecipes(menuItem!!)
        return true
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this,{favorites->
            try {
                for(saved in favorites){
                    if(saved.result.recipeId == args.result.recipeId){
                        changeMenuItemColor(menuItem,R.color.yellow)
                        savedRecipeId = saved.mid
                    }
                    else{
                        changeMenuItemColor(menuItem,R.color.white)
                    }
                }
            }catch (e : Exception){
                Log.d("DetailsActivity",e.message.toString())
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home){
            finish()
        }
        else if(item.itemId==R.id.save_to_favorites_menu && !recipesSaved){
            saveToFavorite(item)
        }
        else if(item.itemId == R.id.save_to_favorites_menu && recipesSaved){
            removeFromFavorites(item)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorite(item: MenuItem) {
        val favoritesEntity =
                FavoritesEntity(
                        0,args.result)
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item,R.color.yellow)
        showSnackBar("Recipe saved")
        recipesSaved = true
    }

    private fun removeFromFavorites(item : MenuItem){
        val favoritesEntity =
                FavoritesEntity(
                        savedRecipeId,
                        args.result
                )
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item,R.color.white)
        showSnackBar("Remove from Favorites.")
        recipesSaved = false
    }

    private fun showSnackBar(s: String) {
        Snackbar.make(
                detailsLayout,
                s,
                Snackbar.LENGTH_LONG
        ).setAction("Okay"){}.show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
            item.icon.setTint(ContextCompat.getColor(this,color))
    }

}