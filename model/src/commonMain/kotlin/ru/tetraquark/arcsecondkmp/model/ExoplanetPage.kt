package ru.tetraquark.arcsecondkmp.model

import kotlinx.serialization.Serializable

@Serializable
data class ExoplanetPage(
    val exoplanets: List<Exoplanet>,
    val page: Int,
    val offset: Int
)
