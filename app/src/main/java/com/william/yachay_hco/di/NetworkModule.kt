package com.william.yachay_hco.di

import com.william.yachay_hco.network.AuthService
import com.william.yachay_hco.network.CulturalService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @LocalApi
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(" https://e4471a3e17aa.ngrok-free.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(@LocalApi retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideCulturalService(@LocalApi retrofit: Retrofit): CulturalService {
        return retrofit.create(CulturalService::class.java)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenAIApi