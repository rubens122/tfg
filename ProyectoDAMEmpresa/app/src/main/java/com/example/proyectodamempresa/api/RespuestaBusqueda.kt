package com.example.proyectodamempresa.api

import com.example.proyectodamempresa.models.Track

data class RespuestaBusqueda(
    val results: Results
)

data class Results(
    val trackmatches: TrackMatches
)

data class TrackMatches(
    val track: List<Track>
)
