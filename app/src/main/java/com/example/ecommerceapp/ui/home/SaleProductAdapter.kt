package com.example.ecommerceapp.ui.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.common.loadImage
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.databinding.ItemProductSaleBinding

class SaleProductAdapter(private val productsaleListener: SaleProductListener) :
    ListAdapter<ProductUI, SaleProductAdapter.SaleProductViewHolder>(ProductDiffCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SaleProductViewHolder =
        SaleProductViewHolder(
            ItemProductSaleBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            productsaleListener
        )


    override fun onBindViewHolder(holder: SaleProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    class SaleProductViewHolder(
        val binding: ItemProductSaleBinding,
        private val productListener: SaleProductListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUI) = with(binding) {
            txtNameSale.text = product.title
            txtPriceSale.text = "${product.price} $"
            imgItemSale.loadImage(product.imageUrl)
            if (product.salePrice < product.price) {
                txtPriceSale.text = "${product.price} $"
                txtPriceSale.paintFlags = txtPriceSale.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                salePriceText.text = "${product.salePrice} $"
            } else {
                salePriceText.visibility = View.GONE
            }
            root.setOnClickListener {
                productListener.onProductClick(product.id)
            }
        }
    }

    class ProductDiffCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }

    interface SaleProductListener {
        fun onProductClick(id: Int)

    }
}

