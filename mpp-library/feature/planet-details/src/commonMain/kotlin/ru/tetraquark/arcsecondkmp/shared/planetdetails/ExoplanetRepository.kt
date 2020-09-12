package ru.tetraquark.arcsecondkmp.shared.planetdetails

import ru.tetraquark.arcsecondkmp.model.Exoplanet

interface ExoplanetRepository {
    suspend fun getExoplanetInfo(name: String): Exoplanet
}
