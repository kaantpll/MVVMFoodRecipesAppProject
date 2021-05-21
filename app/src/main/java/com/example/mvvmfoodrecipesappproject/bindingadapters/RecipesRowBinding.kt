package com.example.mvvmfoodrecipesappproject.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.mvvmfoodrecipesappproject.R
import com.example.mvvmfoodrecipesappproject.model.Result
import com.example.mvvmfoodrecipesappproject.ui.fragments.recipes.RecipesFragmentArgs
import com.example.mvvmfoodrecipesappproject.ui.fragments.recipes.RecipesFragmentDirections

class RecipesRowBinding {

    companion object{

        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(recipeRowLayout : ConstraintLayout , result : Result){
            recipeRowLayout.setOnClickListener {

                try {
                    val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                    recipeRowLayout.findNavController().navigate(action)
                }catch (e : Exception){
                    Log.d("onRecipesClickListener",e.toString())

                }
            }
        }


        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadingFromUrl(imageView:ImageView,imageUrl:String){
            imageView.load(imageUrl){
                crossfade(60)
            }
        }

        @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun setNumberOfLikes(textView: TextView,likes:Int){
            textView.text = likes.toString()
        }

        @BindingAdapter("setNumberOfMinutes")
        @JvmStatic
        fun setNumberOfMinutes(textView:TextView,minutes : Int){
            textView.text = minutes.toString()
        }

        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, vegan : Boolean){
            if(vegan){
                when(view){
                    is TextView -> {
                        view.setTextColor(
                                ContextCompat.getColor(
                                view.context,
                                R.color.green
                                )
                        )
                    }
                    is ImageView ->{
                        view.setColorFilter(
                                ContextCompat.getColor(
                                        view.context,
                                        R.color.green
                                )
                        )
                    }
                }
            }
        }

    }
}