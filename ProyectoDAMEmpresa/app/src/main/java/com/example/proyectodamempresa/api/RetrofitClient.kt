package com.example.proyectodamempresa.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://ws.audioscrobbler.com/2.0/"

    val instance: ServicioLastFM by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ServicioLastFM::class.java)
    }
}