package com.example.cameraapp.ChatbotBackend

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


const val BASE_URL = "REDACTED"


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{


    @Provides
    @Singleton
    fun provideOkHttpClient():OkHttpClient = OkHttpClient.Builder().connectTimeout(300, TimeUnit.SECONDS)
        .readTimeout(300, TimeUnit.SECONDS)
        .writeTimeout(300, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient):Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    @Singleton
    fun provideChatApi(retrofit: Retrofit):ChatApi =
        retrofit.create(ChatApi::class.java)




}