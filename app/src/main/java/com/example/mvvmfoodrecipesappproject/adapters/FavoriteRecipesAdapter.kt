package com.example.mvvmfoodrecipesappproject.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmfoodrecipesappproject.R
import com.example.mvvmfoodrecipesappproject.data.database.entities.FavoritesEntity
import com.example.mvvmfoodrecipesappproject.databinding.FavoriteRecipesRowLayoutBinding
import com.example.mvvmfoodrecipesappproject.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import com.example.mvvmfoodrecipesappproject.util.RecipesDiffUtil
import com.example.mvvmfoodrecipesappproject.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_recipes_row_layout.view.*

class FavoriteRecipesAdapter(
        private val requireActivity: FragmentActivity,
        private val mainViewModel : MainViewModel
        )
    : RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback
{

    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavoritesEntity>()

    private lateinit var mActionMode : ActionMode

    private lateinit var rootView : View

    private var myViewHolders = arrayListOf<MyViewHolder>()
    private var favoriteRecipes = emptyList<FavoritesEntity>()

    class MyViewHolder(private val binding : FavoriteRecipesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(favoritesEntity: FavoritesEntity){
            binding.favoriteEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent : ViewGroup) : MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)

        rootView = holder.itemView.rootView

        val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)

        /**
         * Single click Listener
         * */
        holder.itemView.favoriteRecipesRowLayout.setOnClickListener {
            if(multiSelection){
                applySelection(holder,currentRecipe)
            }else{

                val action = FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                        currentRecipe.result
                )
                holder.itemView.findNavController().navigate(action)
            }
        }

        /**
         * Long Click Listener
         *
         * */
        holder.itemView.favoriteRecipesRowLayout.setOnLongClickListener{
            if(!multiSelection){
                multiSelection=true
                requireActivity.startActionMode(this)
                applySelection(holder,currentRecipe)
                true
            }
            else{
                multiSelection = false
                false
            }
        }
    }

    private fun applySelection(holder : MyViewHolder ,currentRecipe : FavoritesEntity){
        if(selectedRecipes.contains(currentRecipe)){
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)
            applyActionModeTitle()
        }
        else{
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundLightColor,R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder,background:Int,strokeColor : Int){
        holder.itemView.favoriteRecipesRowLayout.setBackgroundColor(
                ContextCompat
                        .getColor(requireActivity,background)
        )
        holder.itemView.favorite_row_cardView.strokeColor = ContextCompat.getColor(requireActivity,strokeColor)
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    private fun applyActionModeTitle(){
        when(selectedRecipes.size){
            0 ->{
                mActionMode.finish()
            }
            1->{
                mActionMode.title = "${selectedRecipes.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedRecipes.size} items selected"

            }
        }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorites_contextual_menu,menu)
        mActionMode = mode!!
        applyStatusBarColor(R.color.contextualActionBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
       return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

        if(item?.itemId == R.id.delete_favorite_recipe_menu){
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe/s removed.")
            multiSelection = false
            selectedRecipes.clear()
            mode?.finish()
        }

        return true

    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolders.forEach { holder->
            changeRecipeStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)
        }
        multiSelection= false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color : Int){
        requireActivity.window.statusBarColor =
                ContextCompat.getColor(requireActivity,color)
    }

    fun setData(newFavoriteRecipes : List<FavoritesEntity>){
        val favoriteRecipesDiffUtil = RecipesDiffUtil(favoriteRecipes,newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)

        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message : String){
        Snackbar.make(
                rootView,
                message,
                Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}.show()
    }

    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }
}