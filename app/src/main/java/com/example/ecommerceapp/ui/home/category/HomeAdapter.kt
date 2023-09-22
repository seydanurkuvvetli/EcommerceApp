package com.example.ecommerceapp.ui.home.category


import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.common.loadImage
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.databinding.ItemProductAllBinding


class HomeAdapter(private val productListener: ProductListener) :
    ListAdapter<ProductUI, HomeAdapter.ProductViewHolder>(ProductDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            ItemProductAllBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            productListener
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ProductViewHolder(
        val binding: ItemProductAllBinding,
        private val productListener: ProductListener

    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUI) = with(binding) {
            tvProductName.text = product.title
            tvProductPrice.text = "${product.price} $"
            if (product.salePrice != 0.0) {
                tvProductPrice.text = "${product.price} $"
                tvProductPrice.paintFlags =
                    tvProductPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG // Üst çizgiyi etkinleştir
                tvProductSale.text = "${product.salePrice} $"
            } else {
                tvProductPrice.text = "${product.price} $"
                tvProductSale.visibility = View.GONE // İndirim yoksa indirimli fiyatı gizle
            }
            imgProduct.loadImage(product.imageUrl)

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

    interface ProductListener {
        fun onProductClick(id: Int)


    }
}