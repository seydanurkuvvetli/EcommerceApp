package com.example.ecommerceapp.data.source.remote

import com.example.ecommerceapp.common.Constans.Endpoint.ADD_TO_CART
import com.example.ecommerceapp.common.Constans.Endpoint.CLEAR_CART
import com.example.ecommerceapp.common.Constans.Endpoint.DELETE_PRODUCT
import com.example.ecommerceapp.common.Constans.Endpoint.GET_CART_PRODUCT
import com.example.ecommerceapp.common.Constans.Endpoint.GET_CATEGORİES
import com.example.ecommerceapp.common.Constans.Endpoint.GET_PRODUCT
import com.example.ecommerceapp.common.Constans.Endpoint.GET_PRODUCTS_BY_CATEGORY
import com.example.ecommerceapp.common.Constans.Endpoint.GET_PRODUCT_DETAİL
import com.example.ecommerceapp.common.Constans.Endpoint.GET_SALE_PRODUCT
import com.example.ecommerceapp.common.Constans.Endpoint.SEARCH_PRODUCT
import com.example.ecommerceapp.data.model.AddToCartRequest
import com.example.ecommerceapp.data.model.ClearCart
import com.example.ecommerceapp.data.model.DeleteRequest
import com.example.ecommerceapp.data.model.GetProductDetailResponse
import com.example.ecommerceapp.data.model.GetProductsResponse
import com.example.ecommerceapp.data.model.Product
import com.example.ecommerceapp.data.model.categories
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductService {
    @GET(GET_PRODUCT)
    suspend fun getProducts(): GetProductsResponse

    @GET(GET_CATEGORİES)
    suspend fun getCategories(): categories

    @GET(GET_PRODUCT_DETAİL)
    suspend  fun getProductDetail(
        @Query("id") id: Int
    ): GetProductDetailResponse


    @GET(GET_SALE_PRODUCT)
    suspend fun getSaleProduct():GetProductsResponse

    @POST(ADD_TO_CART)
     suspend fun addToCart(
        @Body addToCartRequest: AddToCartRequest
    ):GetProductDetailResponse

    @POST(DELETE_PRODUCT)
   suspend fun deleteProduct(
        @Body deleteProduct: DeleteRequest
    ):GetProductsResponse

    @GET(GET_CART_PRODUCT)
    suspend fun  getCartProduct(
        @Query("userId") userId:String
    ):GetProductsResponse
    @GET(SEARCH_PRODUCT)
    suspend fun searchProduct(
        @Query("query") query:String
    ):GetProductsResponse
@POST(CLEAR_CART)
suspend fun clearCart(
    @Body clearCart: ClearCart
):GetProductsResponse

    @GET(GET_PRODUCTS_BY_CATEGORY)
    suspend fun  getProductsByCategory(
        @Query("category") category:String
    ):GetProductsResponse


}



