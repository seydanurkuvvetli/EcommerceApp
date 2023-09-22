package com.example.ecommerceapp.ui.search

import com.example.ecommerceapp.databinding.ItemSearchProductBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.common.loadImage
import com.example.ecommerceapp.data.model.ProductUI



class SearchAdapter(private val productListener: ProductListener) :
    ListAdapter<ProductUI, SearchAdapter.ProductViewHolder>(ProductDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            ItemSearchProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            productListener
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(getItem(position))


    class ProductViewHolder(
        val binding: ItemSearchProductBinding,
        private val productListener: ProductListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUI) = with(binding) {
            tvTitle.text = product.title
            tvPrice.text = "${product.price} ₺"
            imgProduct.loadImage(product.imageUrl)
            ratingBar.rating = product.rate.toFloat()
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