package ru.tetraquark.arcsecondkmp.shared.data

import ru.tetraquark.arcsecondkmp.database.Database
import ru.tetraquark.arcsecondkmp.model.Exoplanet
import ru.tetraquark.arcsecondkmp.model.ExoplanetPage
import ru.tetraquark.arcsecondkmp.shared.planetslist.ExoplanetRepository as PlanetsListExoplanetRepository
import ru.tetraquark.arcsecondkmp.shared.planetdetails.ExoplanetRepository as PlanetDetailsExoplanetRepository

internal interface ExternalExoplanetRepository : PlanetsListExoplanetRepository,
    PlanetDetailsExoplanetRepository

class ExoplanetRepository(
    private val api: ArcsecondApi,
    private val database: Database
) : ExternalExoplanetRepository {

    override suspend fun loadExoplanets(): List<Exoplanet> {
        return getExoplanets(0, 10).exoplanets
    }

    override suspend fun getExoplanetInfo(name: String): Exoplanet {
        return getExoplanetByName(name)
    }

    private suspend fun getExoplanets(page: Int, offset: Int): ExoplanetPage {
        return api.getExoplanets(page, offset).also {
            database.insertExoplanets(it.exoplanets)
        }
    }

    private suspend fun getExoplanetByName(name: String): Exoplanet {
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
