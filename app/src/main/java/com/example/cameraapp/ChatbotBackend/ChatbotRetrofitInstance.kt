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

//object ChatbotRetrofitInstance {
//    private val BASE_URL = "https://f838c2b6b739.ngrok-free.app"
//    val okHttpClient = OkHttpClient.Builder()
//        .connectTimeout(30, TimeUnit.SECONDS)   // Connection timeout
//        .readTimeout(60, TimeUnit.SECONDS)      // Read timeout (waiting for server to respond)
//        .writeTimeout(60, TimeUnit.SECONDS)     // Write timeout (if uploading data)
//        .build()
//
//
//    val api : ChatApi by lazy{
//        retrofit2.Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(okHttpClient)
//            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
//            .build()
//            .create(ChatApi::class.java)
//    }
//}
const val BASE_URL = "https://b019e1bdecb1.ngrok-free.app"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{


    @Provides
    @Singleton
    fun provideOkHttpClient():OkHttpClient = OkHttpClient.Builder().connectTimeout(6000, TimeUnit.SECONDS)   // Connection timeout
        .readTimeout(3000, TimeUnit.SECONDS)      // Read timeout (waiting for server to respond)
        .writeTimeout(3000, TimeUnit.SECONDS)     // Write timeout (if uploading data)
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