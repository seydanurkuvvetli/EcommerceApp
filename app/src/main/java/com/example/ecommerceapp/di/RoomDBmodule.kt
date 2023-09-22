package com.example.ecommerceapp.di

import android.content.Context
import androidx.room.Room
import com.example.ecommerceapp.data.source.local.ProductRoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object RoomDBmodule {
    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context: Context) =
      Room.databaseBuilder(
            context,
            ProductRoomDB::class.java,
            "product_room_db"
        ).build()


    @Provides
    @Singleton
    fun provideProductDao(RoomDB:ProductRoomDB) =RoomDB.productDao()
    }