package com.example.dlmtech.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Corrigido: Pasta correta é 'dlmtech_api' e não 'api_dlmtech'
    private const val BASE_URL = "http://192.168.56.1:8081/api_dlmtech/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}