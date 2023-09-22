package com.example.ecommerceapp.data.model

import com.google.gson.annotations.SerializedName

data class ProductDetail(
    val id:Int?,
    val title:String?,
    val price:Double?,
    val salePrice:Double?,
    val description:String?,
    val category:String?,
    @SerializedName("imageOne") val imageUrl:String?,
    @SerializedName("imageTwo") val imageTwo:String?,
    @SerializedName("imageThree") val imageThree:String?,
    val rate:Double?,
    val count:Int?,
    @SerializedName("saleState") val bestSeller:Boolean?,
)
