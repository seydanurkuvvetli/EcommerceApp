package com.example.ecommerceapp.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.common.loadImage
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.databinding.ItemFavoriteBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FavoriteAdapter(private val favoriteListener: FavoriteListener) :
    ListAdapter<ProductUI, FavoriteAdapter.FavoriteViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder =
        FavoriteViewHolder(
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            favoriteListener
        )

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) =
        holder.bind(getItem(position))


    class FavoriteViewHolder(
        val binding: ItemFavoriteBinding,
        private val favoriteListener: FavoriteListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUI) = with(binding) {
            val user = Firebase.auth.currentUser
            val userId = user?.uid
            tvTitle.text = product.title
            tvPrice.text = "${product.price} ₺"
            imgProduct.loadImage(product.imageUrl)
            ratingBar.rating = product.rate.toFloat()
            root.setOnClickListener {
                favoriteListener.onProductClick(product.id)
            }
            imgDelete.setOnClickListener {
                if (userId != null) {
                    favoriteListener.onDeleteClick(product)
                }
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

    interface FavoriteListener {
        fun onProductClick(id: Int)
        fun onDeleteClick(product: ProductUI)


    }
}