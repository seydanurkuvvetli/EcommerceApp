package com.example.ecommerceapp.ui.home

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.common.gone
import com.example.ecommerceapp.common.visible
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.ui.home.category.CategoryPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), SaleProductAdapter.SaleProductListener {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val saleProductAdapter by lazy { SaleProductAdapter(this) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSale.adapter = saleProductAdapter
        viewModel.getSaleProduct()
        viewModel.getCategory()
        observeSaleProduct()
    }
    private fun observeSaleProduct() {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                HomeState.Loading -> {
                    binding.progressBar.visible()
                }
                is HomeState.Data -> {
                    binding.progressBar.gone()
                    saleProductAdapter.submitList(state.products)
                }
                is HomeState.Error -> {
                    binding.progressBar.gone()
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
                else -> {}
            }
        }
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                HomeState.Loading -> {
                    binding.progressBar.visible()
                }
                is HomeState.Data2 -> {
                    binding.progressBar.gone()
                    val tempCategories = state.products.toMutableList()
                    tempCategories.add(0, "All")
                    binding.vpCategory.adapter =
                        CategoryPagerAdapter(this@HomeFragment, tempCategories)
                    TabLayoutMediator(binding.tabLayout2,binding.vpCategory) { tab, position ->
                        tab.text = tempCategories[position]
                    }.attach()
                }
                is HomeState.Error -> {
                    binding.progressBar.gone()
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
                else -> {}
            }
        }
    }

    override fun onProductClick(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }
}