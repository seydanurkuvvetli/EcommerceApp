package com.example.ecommerceapp.data.model



data class ProductUI(val id:Int,
                     val title:String,
                     val price:Double,
                     val salePrice:Double,
                     val description:String,
                     val category:String,
                     val imageUrl:String,
                     val imageTwo:String,
                     val imageThree:String,
                     val rate:Double,
                     val count:Int,
                      val bestSeller:Boolean
)
