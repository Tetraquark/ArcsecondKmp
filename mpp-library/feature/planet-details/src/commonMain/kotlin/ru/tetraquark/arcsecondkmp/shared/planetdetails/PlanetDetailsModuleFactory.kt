package ru.tetraquark.arcsecondkmp.shared.planetdetails

class PlanetDetailsModuleFactory(
    private val exoplanetRepository: ExoplanetRepository
) {
    fun createPlanetDetailsViewModel(planetName: String): PlanetDetailsViewModel {
        return PlanetDetailsViewModel(exoplanetRepository, planetName)
    }
}
