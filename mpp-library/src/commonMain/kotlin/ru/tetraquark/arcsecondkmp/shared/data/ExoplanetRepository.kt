package ru.tetraquark.arcsecondkmp.shared.data

import ru.tetraquark.arcsecondkmp.database.Database
import ru.tetraquark.arcsecondkmp.model.Exoplanet
import ru.tetraquark.arcsecondkmp.model.ExoplanetPage

class ExoplanetRepository(
    private val api: ArcsecondApi,
    private val database: Database
) {

    suspend fun getExoplanets(page: Int, offset: Int): ExoplanetPage {
        return api.getExoplanets(page, offset).also {
            database.insertExoplanets(it.exoplanets)
        }
    }

    suspend fun getExoplanetByName(name: String): Exoplanet {
        return database.getExoplanet(name)?.let {
            if (api.checkExoplanet(it.name, it.hashCode()).isValid) {
                it
            } else {
                null
            }
        } ?: api.getExoplanetByName(name).also {
            database.insertExoplanet(it)
        }
    }
}
