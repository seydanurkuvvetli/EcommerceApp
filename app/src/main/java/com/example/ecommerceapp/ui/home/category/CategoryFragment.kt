package com.example.ecommerceapp.ui.home.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.common.Constans.Endpoint.CATEGORY
import com.example.ecommerceapp.common.Constans.Endpoint.GET_CATEGORİES
import com.example.ecommerceapp.common.gone
import com.example.ecommerceapp.common.viewBinding
import com.example.ecommerceapp.common.visible
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.databinding.FragmentCategoryBinding
import com.example.ecommerceapp.databinding.FragmentFavoriteBinding
import com.example.ecommerceapp.ui.home.HomeFragmentDirections

import com.example.ecommerceapp.ui.home.HomeState
import com.example.ecommerceapp.ui.home.HomeViewModel
import com.example.ecommerceapp.ui.home.SaleProductAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category), HomeAdapter.ProductListener {
    private val binding by viewBinding(FragmentCategoryBinding::bind)
    private val viewModel: CategoryProductsViewModel by viewModels()
    private val categoryProductsAdapter by lazy { HomeAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(CATEGORY) }?.apply {
            getString(CATEGORY)?.let {
                if (it == "All") viewModel.getProduct()
                else viewModel.getProductsByCategory(it)
            }
        }
        observeProductByCategory()
    }
    private fun observeProductByCategory() {
        viewModel.categoryState.observe(viewLifecycleOwner) { state ->
            when (state) {
                CategoryProductsViewModel.CategoryState.Loading -> {

                }
                is CategoryProductsViewModel.CategoryState.Data -> {
                    categoryProductsAdapter.submitList(state.products)
                    binding.rvCategory.adapter = categoryProductsAdapter
                }

                is CategoryProductsViewModel.CategoryState.Error -> {
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
