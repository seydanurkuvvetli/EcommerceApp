package com.example.ecommerceapp.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.ecommerceapp.common.Constans.BASE_URL
import com.example.ecommerceapp.data.source.remote.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val TIMEOUT = 60L
@Provides
@Singleton
     fun getHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request =
                chain.request().newBuilder()
                    .addHeader("store", "fashionstore")
                    .build()
            chain.proceed(request)
        }
    }



    @Provides
    @Singleton
    fun provideChuckerInterceptor(@ApplicationContext context: Context) = ChuckerInterceptor.Builder(context).build()

    @Provides
    @Singleton
    fun provideOkHttpClient(chuckerInterceptor: ChuckerInterceptor) = OkHttpClient.Builder().apply {
        addInterceptor(chuckerInterceptor)
        addInterceptor(getHeaderInterceptor())
        readTimeout(TIMEOUT, TimeUnit.SECONDS)
        connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(TIMEOUT, TimeUnit.SECONDS)
    }.build()
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
    .build()

    @Provides
    @Singleton
    fun provideBookService(retrofit: Retrofit) = retrofit.create<ProductService>()

    }