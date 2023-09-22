package com.example.ecommerceapp.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.common.gone
import com.example.ecommerceapp.common.visible
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.databinding.FragmentSearchBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), SearchAdapter.ProductListener {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private val productAdapter by lazy { SearchAdapter(this) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSearchProducts.adapter = productAdapter



        observeData()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if ((newText?.length ?: 0) >= 3) {
                    viewModel.getProduct(newText ?: "")
                }
                return false
            }
        })

    }
    private fun observeData() {
        viewModel.searchState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchState.Loading -> {
                    binding.lottieAnimationView.visible()
                }
                is SearchState.Data -> {

                    binding.lottieAnimationView.gone()
                    productAdapter.submitList(state.products)
                }
                is SearchState.Error -> {
                    binding.lottieAnimationView.gone()
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
            }
        }
    }
    override fun onProductClick(id: Int) {
        val action = SearchFragmentDirections.actionSearchFragment2ToDetailFragment(id)
        findNavController().navigate(action)
    }


}