package com.example.ecommerceapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import dagger.Provides

@Entity(tableName = "fav_products" )
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id:Int?,
    @ColumnInfo("userId")
    val userId:String?,
    @ColumnInfo("title")
    val title:String?,
    @ColumnInfo("price")
    val price:Double?,
    @ColumnInfo("salePrice")
    val salePrice:Double?,
    @ColumnInfo("description")
    val description:String?,
    @ColumnInfo("category")
    val category:String?,
    @ColumnInfo("imageOne")
    val imageOne:String?,
    @ColumnInfo("imageTwo")
    val imageTwo:String?,
    @ColumnInfo("imageThree")
    val imageThree:String?,
    @ColumnInfo("rate")

    val rate:Double?,
    @ColumnInfo("count")
    val count:Int?,
    @ColumnInfo("saleState")
    val saleState:Boolean?,

    )
