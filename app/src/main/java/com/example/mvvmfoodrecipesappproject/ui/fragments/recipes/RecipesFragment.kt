package com.example.mvvmfoodrecipesappproject.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmfoodrecipesappproject.viewmodels.MainViewModel
import com.example.mvvmfoodrecipesappproject.R
import com.example.mvvmfoodrecipesappproject.adapters.RecipesAdapter
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.API_KEY
import com.example.mvvmfoodrecipesappproject.util.NetworkResult
import com.example.mvvmfoodrecipesappproject.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var mView : View
    private val mAdapter by lazy {RecipesAdapter()}
    private lateinit var recipesViewModel : RecipesViewModel
    private lateinit var mainViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_recipes, container, false)


        setupRecyclerView()

        requestApiData()


        return mView
    }

    private fun requestApiData(){

        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner,{response->
            when(response){
                is NetworkResult.Success->
                {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error->{
                    hideShimmerEffect()
                    Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading ->{
                    showShimmerEffect()
                }
            }
        })

    }



    private fun setupRecyclerView(){
        mView.recyclerview.adapter = mAdapter
        mView.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect(){
        mView.recyclerview.showShimmer()
    }

    private fun hideShimmerEffect(){
        mView.recyclerview.hideShimmer()
    }



}