package com.example.ecommerceapp.ui.cart

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.common.gone
import com.example.ecommerceapp.common.visible
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.databinding.FragmentCartBinding
import com.example.ecommerceapp.ui.home.CartAdapter
import com.example.ecommerceapp.ui.home.CartState
import com.example.ecommerceapp.ui.home.CartViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart), CartAdapter.CartListener {
    private lateinit var binding: FragmentCartBinding
    private val viewModel by viewModels<CartViewModel>()
    private lateinit var sharedPreferences: SharedPreferences
    private val cartAdapter by lazy { CartAdapter(this) }
    private val user = Firebase.auth.currentUser
    private val userId = user?.uid
    private var totalAmount: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(
            "com.example.ecommerceapp",
            Context.MODE_PRIVATE
        )

        setupUI()
        observeData()
    }

    private fun setupUI() {
        binding.rvCartProducts.adapter = cartAdapter
        val isCartEmpty = cartAdapter.itemCount == 0
        sharedPreferences.edit().putBoolean("isProductInBag", isCartEmpty).apply()

        if (userId != null) {
            viewModel.getCartProducts(userId)
        }

        with(binding) {
            btnClear.setOnClickListener {
                clearCart()
            }



            btnBuyNow.setOnClickListener {
                if (cartAdapter.itemCount==0) {
                    showEmptyCartDialog()
                } else {
                    val action = CartFragmentDirections.actionCartFragmentToPaymentFragmentt()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun clearCart() {

            userId?.let { uid ->
                lifecycleScope.launch {
                    viewModel.clearCart(uid)
                    viewModel.getCartProducts(uid)
                }
            }
            totalAmount = 0.0
            binding.tvTotalAmount.text = "${0.0} $"
        }




    private fun showEmptyCartDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Uyarı")
            .setMessage("Sepet boş")
            .setNegativeButton("Tamam") { dialog, _ ->
                findNavController().navigate(R.id.cartFragment)
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun observeData() {
        viewModel.cartState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CartState.Loading -> {
                    binding.progressBar.visible()
                }
                is CartState.Data -> {
                    binding.progressBar.gone()
                    viewModel.updateCartState()
                    cartAdapter.submitList(state.products)
                    totalAmount = state.products.sumOf {
                        if (it.salePrice == 0.0) {
                            it.price
                        } else {
                            it.salePrice
                        }
                    }
                    binding.tvTotalAmount.text = String.format("%.3f$", totalAmount)

                }
                is CartState.Data2 -> {
                    Snackbar.make(requireView(), state.products, 1000).show()
                }
                is CartState.Error -> {
                    binding.progressBar.gone()
                    binding.rvCartProducts.gone()
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
                else -> {}
            }
        }
    }

    override fun onProductClick(id: Int) {
        val action = CartFragmentDirections.actionCartFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }

    override fun onDeleteClick(product: ProductUI) {
        userId?.let { uid ->
            lifecycleScope.launch {
                viewModel.removeFromCart(product.id, uid)
            }
        }
    }

    override fun onIncreaseClick(price: Double) {
        viewModel.increase(price)
    }

    override fun onDecreaseClick(price: Double) {
        viewModel.decrease(price)
    }
}
