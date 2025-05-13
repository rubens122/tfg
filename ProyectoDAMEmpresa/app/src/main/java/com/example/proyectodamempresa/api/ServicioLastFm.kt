package com.example.proyectodamempresa.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServicioLastFM {
    @GET("?method=track.search&format=json")
    fun buscarCanciones(
        @Query("track") track: String,
        @Query("api_key") apiKey: String
    ): Call<RespuestaBusqueda>
}