package com.example.ecommerceapp.di

import com.example.ecommerceapp.data.repository.ProductsRepository
import com.example.ecommerceapp.data.repository.UserRepository
import com.example.ecommerceapp.data.source.local.ProductDao
import com.example.ecommerceapp.data.source.remote.ProductService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideProductRepository(productService: ProductService,productDao: ProductDao): ProductsRepository =
        ProductsRepository(productService,productDao)
    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth,firestore: FirebaseFirestore): UserRepository {
        return UserRepository(auth,firestore)
    }
}