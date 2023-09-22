package com.example.ecommerceapp.ui.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.common.gone
import com.example.ecommerceapp.common.loadImage
import com.example.ecommerceapp.common.visible
import com.example.ecommerceapp.data.model.Product
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.databinding.ItemCartProductBinding
import com.google.android.material.snackbar.Snackbar


class CartAdapter(private val cartListener: CartListener) :
    ListAdapter<ProductUI, CartAdapter.CartViewHolder>(CartDiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder =
        CartViewHolder(
            ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            cartListener

        )

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) =
        holder.bind(getItem(position))


    class CartViewHolder(
        val binding: ItemCartProductBinding,
        private val cartListener: CartListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) = with(binding) {
            var productCount = 1
            if (productCount == 1) {
                tvProductCount.text = productCount.toString()
            }
            tvProductName.text = product.title


            if (product.salePrice !=0.0) {
                tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG // Üst çizgiyi etkinleştir
                tvPriceSaleCart.text = "${product.salePrice} $"
            } else {
                tvPrice.text = "${product.price} $"
                tvPriceSaleCart.visibility = View.GONE // İndirim yoksa indirimli fiyatı gizle
            }
            imgProduct.loadImage(product.imageUrl)

            root.setOnClickListener {
                cartListener.onProductClick(product.id)
            }
            imgDelete.setOnClickListener {

                cartListener.onDeleteClick(product)

            }



            imgDecrease.setOnClickListener {
                if (productCount != 1) {
                    cartListener.onDecreaseClick(product.price)
                    productCount--
                    tvProductCount.text = productCount.toString()
                } else {
                    cartListener.onDeleteClick(product)
                }

            }
            imgIncrease.setOnClickListener {
                cartListener.onIncreaseClick(product.price)
                productCount++
                tvProductCount.text = productCount.toString()

            }


        }


    }

    class CartDiffCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }

    interface CartListener {
        fun onProductClick(id: Int)
        fun onDeleteClick(product: ProductUI)
        fun onIncreaseClick(price: Double)
        fun onDecreaseClick(price: Double)




    }
}

