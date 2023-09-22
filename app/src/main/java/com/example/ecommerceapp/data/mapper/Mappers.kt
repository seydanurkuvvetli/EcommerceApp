package com.example.ecommerceapp.data.mapper


import com.example.ecommerceapp.data.model.Product
import com.example.ecommerceapp.data.model.ProductDetail
import com.example.ecommerceapp.data.model.ProductEntity
import com.example.ecommerceapp.data.model.ProductUI

fun ProductDetail.mapToProductUI(): ProductUI {
    return ProductUI(
        id = id ?: 1,
        title = title.orEmpty(),
        price = price ?: 0.0,
        salePrice = salePrice ?: 0.0,
        description = description.orEmpty(),
        category = category.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        imageTwo = imageUrl.orEmpty(),
        imageThree = imageUrl.orEmpty(),
        rate = rate ?: 0.0,
        count = count ?: 1,
        bestSeller = bestSeller ?: true,


        )
}


fun Product.mapToProductUI(): ProductUI {
    return ProductUI(
        id = id ?: 1,
        title = title.orEmpty(),
        price = price ?: 0.0,
        description = description.orEmpty(),
        category = category.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        imageTwo = imageTwo.orEmpty(),
        imageThree = imageThree.orEmpty(),
        rate = rate ?: 0.0,
        count = count ?: 1,
        salePrice = salePrice ?: 0.0,
        bestSeller = bestSeller ?: true,
    )

}

fun ProductUI.mapToProductEntity(userId:String): ProductEntity {

    return ProductEntity(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        imageOne = imageUrl,
        imageTwo = imageTwo,
        imageThree = imageThree,
        rate = rate,
        count = count,
        salePrice = salePrice,
        saleState = bestSeller,
userId =userId



    )
}


fun ProductEntity.mapToProductUI(): ProductUI {
    return ProductUI(
        id = id ?: 1,
        title = title.orEmpty(),
        price = price ?: 0.0,
        description = description.orEmpty(),
        category = category.orEmpty(),
        imageUrl = imageOne.orEmpty(),
        imageTwo = imageTwo.orEmpty(),
        imageThree = imageThree.orEmpty(),
        rate = rate ?: 0.0,
        count = count ?: 1,
        salePrice = salePrice ?: 0.0,
        bestSeller = saleState ?: true,




        )
}
