package com.example.ecommerceapp.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerceapp.data.model.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM fav_products WHERE userId=:userId")
    suspend fun getProducts(userId:String): List<ProductEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addProduct(product:ProductEntity)

    @Query("DELETE FROM fav_products WHERE id = :pid")
    suspend fun deleteFavoritesByUidAndPid( pid: Int)

    @Query("SELECT * FROM fav_products WHERE id = :productId AND userId=:userId")
    suspend fun getFavoriteProductById(productId: Int,userId: String): ProductEntity?

}