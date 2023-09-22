package com.example.ecommerceapp.data.repository


import android.annotation.SuppressLint
import com.example.ecommerceapp.common.Resource
import com.example.ecommerceapp.data.mapper.mapToProductEntity
import com.example.ecommerceapp.data.mapper.mapToProductUI
import com.example.ecommerceapp.data.model.AddToCartRequest
import com.example.ecommerceapp.data.model.ClearCart
import com.example.ecommerceapp.data.model.DeleteRequest
import com.example.ecommerceapp.data.model.ProductEntity
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.data.source.local.ProductDao

import com.example.ecommerceapp.data.source.remote.ProductService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProductsRepository(
    private val productService: ProductService,
    private val productDao: ProductDao
) {
    suspend fun getProducts(): Resource<List<ProductUI>> {
        return try {
            val result = productService.getProducts()
            if (result.status != 200) {
                Resource.Error(Exception(result.message.orEmpty()))
            } else {
                Resource.Success(result.products!!.map {
                    it.mapToProductUI()
                })
            }

        } catch (e: Exception) {
            Resource.Error(e)
        }

    }

    suspend fun getProductsByCategory(category: String): Resource<List<ProductUI>> {
        return try {
            val result = productService.getProductsByCategory(category)
            if (result.status != 200) {
                Resource.Error(Exception(result.message.orEmpty()))
            } else {
                Resource.Success(result.products!!.map {
                    it.mapToProductUI()
                })
            }

        } catch (e: Exception) {
            Resource.Error(e)
        }

    }

    suspend fun getSaleProducts(): Resource<List<ProductUI>> {
        val result = productService.getSaleProduct()
        return try {
            if (result.status == 200) {
                Resource.Success(result.products!!.map {
                    it.mapToProductUI()
                })

            } else {
                Resource.Error(Exception(result.message.orEmpty()))
            }

        } catch (e: Exception) {
            Resource.Error(e)
        }

    }

    suspend fun getCategories(): Resource<List<String>> {

        return try {

            val response = productService.getCategories()

            if (response.status == 200) {

                Resource.Success(response.categories!!)
            } else {
                Resource.Error(Exception(response.message.orEmpty()))
            }
        } catch (e: Exception) {

            Resource.Error(e)
        }

    }

    private var totalAmount: Double = 0.0

    fun getTotalAmount(): Double {
        return totalAmount
    }

    fun addToTotalAmount(price: Double) {
        totalAmount += price
    }

    fun removeFromTotalAmount(price: Double) {
        totalAmount -= price
    }

    suspend fun addToFavorites(userId: String, product: ProductUI) {
        val productEntity = product.mapToProductEntity(userId)
        productDao.addProduct(productEntity)
    }

    suspend fun deleteFavorites(productId: Int) {
        productDao.deleteFavoritesByUidAndPid(productId)
    }

    suspend fun isProductFavorite(userId: String, productId: Int): Boolean {
        val favoriteProduct = productDao.getFavoriteProductById(productId, userId)
        return favoriteProduct != null
    }

    suspend fun deleteCartProduct(id: Int):Resource<String>{
        try{
    val result= productService.deleteProduct(DeleteRequest(id))
            if (result.status == 200) {
                return Resource.Success(result.message.orEmpty())
            } else {
                return Resource.Error(Exception(result.message.orEmpty()))
            }
        } catch (e: Exception) {
            return Resource.Error(e)
        }

    }

  suspend  fun clearCart(userId: String):Resource<String> {
        return try {

            val result= productService.clearCart(ClearCart(userId))
            if(result.status==200){
                Resource.Success(result.message.orEmpty())
            }else{
                Resource.Error(Exception(result.message.orEmpty()))

            }}catch (
            e: Exception
        ){
            return Resource.Error(e)
        }
    }

    suspend fun addToCart(userId: String, productId: Int):Resource<String> {
      return try {


        val result=productService.addToCart(AddToCartRequest(userId, productId))
        if(result.success==200){
            Resource.Success(result.message.orEmpty())
        }else{
            Resource.Error(Exception(result.message.orEmpty()))

        }}catch (
            e: Exception
        ){
            return Resource.Error(e)
        }
    }


    suspend fun getProductDetail(id: Int): Resource<ProductUI> {
        try {
            val result = productService.getProductDetail(id).product
                if (result != null) {
                    val productUI = result.mapToProductUI() // API yanıtını UI modeline dönüştür

                    return Resource.Success(productUI)
                } else {
                    return Resource.Error(Exception("Ürün bulunamadı"))
                }

        } catch (e: Exception) {
            return Resource.Error(e)
        }
    }


    suspend fun searchProduct(query: String): Resource<List<ProductUI>> {
        val result = productService.searchProduct(query)
        return try {
            if (result.status != 200) {
                Resource.Error(Exception(result.message.orEmpty()))

            } else {
                Resource.Success(result.products!!.map {
                    it.mapToProductUI()
                })
            }


        } catch (e: Exception) {
            Resource.Error(e)
        }

    }


    suspend fun getProductFav(userId: String): Resource<List<ProductUI>> {
        return try {
            run {
                val result = productDao.getProducts(userId).map { it.mapToProductUI() }

                Resource.Success(result)
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }


    suspend fun getCartProduct(userId: String): Resource<List<ProductUI>> {
        return try {
            val result = productService.getCartProduct(userId)
            if (result.status != 200) {
                Resource.Error(Exception(result.message.orEmpty()))
            } else {
                Resource.Success(result.products!!.map {
                    it.mapToProductUI()
                })
            }

        } catch (e: Exception) {
            Resource.Error(e)
        }
    }


}