package com.example.ecommerceapp.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.common.gone
import com.example.ecommerceapp.common.visible
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.databinding.FragmentFavoriteBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite), FavoriteAdapter.FavoriteListener {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewmodel by viewModels<FavoriteViewModel>()
    private val favoriteAdapter by lazy { FavoriteAdapter(this) }
    val user = Firebase.auth.currentUser
    val userId = user?.uid
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        if (userId != null) {
            viewmodel.getCartProducts(userId)
        }
        binding.rvFavorites.adapter = favoriteAdapter
    }
    private fun observeData() = with(binding) {
        viewmodel.favState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavState.Loading -> {
                    progressBar.visible()
                }
                is FavState.Data -> {
                    progressBar.gone()
                    favoriteAdapter.submitList(state.products)
                }
                is FavState.Error -> {
                    progressBar.gone()
                    rvFavorites.gone()
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
            }
        }
    }
    override fun onProductClick(id: Int) {
        val action = FavoriteFragmentDirections.favToDetail(id)
        findNavController().navigate(action)
    }
    override fun onDeleteClick(product: ProductUI) {
        if (userId != null) {
            viewmodel.deleteProductFromCart(product)
        }
    }
}