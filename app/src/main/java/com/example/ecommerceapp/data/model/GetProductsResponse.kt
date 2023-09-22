package com.example.ecommerceapp.data.model

data class GetProductsResponse(
    val status:Int?,
    val message:String?,
    val products:List<Product>?
)
