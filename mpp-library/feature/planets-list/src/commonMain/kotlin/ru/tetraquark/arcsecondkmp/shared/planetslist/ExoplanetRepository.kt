package ru.tetraquark.arcsecondkmp.shared.planetslist

import ru.tetraquark.arcsecondkmp.model.Exoplanet

interface ExoplanetRepository {
    suspend fun loadExoplanets(): List<Exoplanet>
}
