package com.example.ecommerceapp.ui.detail


import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ecommerceapp.R
import com.example.ecommerceapp.common.gone
import com.example.ecommerceapp.common.loadImage
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.databinding.FragmentDetailBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val viewModel by viewModels<DetailViewModel>()
    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var sharedPreferences: SharedPreferences
    private val auth = Firebase.auth
    private val userId = auth.currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = view.context.getSharedPreferences(
            "favicon",
            Context.MODE_PRIVATE
        )
        if (userId != null) {
            viewModel.getDetailProducts(args.id)
        }
        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.detailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DetailViewModel.DetailState.Loading -> {
                    // binding.progressBar.visible()
                }

                is DetailViewModel.DetailState.Data -> {
                    with(state.products) {
                        val product = ProductUI(
                            id,
                            title,
                            price,
                            salePrice,
                            description,
                            category,
                            imageUrl,
                            imageUrl,
                            imageThree,
                            rate,
                            count,
                            bestSeller
                        )
                        textName.text = title
                        textPrice.text = "${state.products.price} $"
                        descriptiondetail.text = description
                        imageItem.loadImage(imageUrl)
                        ratingBar.rating = rate.toFloat()

                        if (product.salePrice != 0.0) {
                            textPrice.text = "${product.price} $"
                            textPrice.paintFlags =
                                textPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            textPricedetailSale.text = "${product.salePrice} $"
                        } else {

                            textPricedetailSale.gone()
                        }
                        back.setOnClickListener { findNavController().navigateUp() }

                        setupFavoriteButton(product)
                        setupAddToBasketButton(product.id)

                    }
                }
                is DetailViewModel.DetailState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
                else -> {}
            }
        }
    }

    private fun setupFavoriteButton(product: ProductUI) {
        with(binding.favoriteButton) {
            if (userId != null) {
                viewModel.checkFavoriteState(userId, product.id)
            }
            setOnClickListener {
                if (userId != null) {
                    viewModel.toggleFavoriteProduct(product)
                }
            }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            binding.favoriteButton.setFavoriteButtonColor(userId, product.id)
        }
    }

    private fun View.setFavoriteButtonColor(userId: String?, productId: Int) {
        val isFavorite = viewModel.isFavorite.value == true
        backgroundTintList = ContextCompat.getColorStateList(
            context,
            if (isFavorite) R.color.red else R.color.light_red
        )
    }

    private fun setupAddToBasketButton(productId: Int) {
        binding.addToBasketButton.setOnClickListener {
            auth.currentUser?.let { user ->
                userId?.let { uid ->
                    lifecycleScope.launch { viewModel.addProductToBasket(uid, productId) }
                }
            }
        }
    }

}