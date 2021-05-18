package com.example.mvvmfoodrecipesappproject.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmfoodrecipesappproject.viewmodels.MainViewModel
import com.example.mvvmfoodrecipesappproject.R
import com.example.mvvmfoodrecipesappproject.adapters.RecipesAdapter
import com.example.mvvmfoodrecipesappproject.databinding.FragmentRecipesBinding
import com.example.mvvmfoodrecipesappproject.util.Constants.Companion.API_KEY
import com.example.mvvmfoodrecipesappproject.util.NetworkResult
import com.example.mvvmfoodrecipesappproject.util.observeOnce
import com.example.mvvmfoodrecipesappproject.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private var _binding : FragmentRecipesBinding? = null
    private val binding get()= _binding!!

    private val args by navArgs<RecipesFragmentArgs>()

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

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.mainViewModel = mainViewModel

        setupRecyclerView()

        readDatabase()

        binding.recipesFab.setOnClickListener {
           findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
        }


        return binding.root
    }

    private fun readDatabase() {
        mainViewModel.readRecipes.observeOnce(viewLifecycleOwner,{database->
            if(database.isNotEmpty() && !args.backFromBottomSheet)
            {
                mAdapter.setData(database[0].foodRecipes)
                hideShimmerEffect()
            }
            else{
                requestApiData()
            }
        })
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
                    loadDataFromCache()
                    Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading ->{
                    showShimmerEffect()
                }
            }
        })

    }


    private fun loadDataFromCache(){
      lifecycleScope.launch {
          mainViewModel.readRecipes.observe(viewLifecycleOwner,{
              database->
              if(database.isNotEmpty()){
                  mAdapter.setData(database[0].foodRecipes)
              }
          })
      }
    }


    private fun setupRecyclerView(){
      lifecycleScope.launch {
          binding.recyclerview.adapter = mAdapter
          binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
          showShimmerEffect()
      }
    }

    private fun showShimmerEffect(){
        binding.recyclerview.showShimmer()
    }

    private fun hideShimmerEffect(){
        binding.recyclerview.hideShimmer()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}