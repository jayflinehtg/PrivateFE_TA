package com.example.myapplication.services

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitClient @Inject constructor(
    private val context: Context,
    private val tokenRefreshInterceptor: TokenRefreshInterceptor
) {

    companion object {
        const val BASE_URL = "http://172.26.42.79:5000/api/"
        const val SMART_CONTRACT_ADDRESS = "0xBea1D42A4C0138A484C432BBBa296Ff320D5700B"
        const val PRIVATE_RPC_URL = "http://172.21.200.101:8545"
    }

    private val retrofitInstance: Retrofit by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenRefreshInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRetrofit(): Retrofit = retrofitInstance
}