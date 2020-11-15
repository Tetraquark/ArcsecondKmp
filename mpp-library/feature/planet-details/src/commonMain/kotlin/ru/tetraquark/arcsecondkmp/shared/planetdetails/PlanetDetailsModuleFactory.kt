package ru.tetraquark.arcsecondkmp.shared.planetdetails

class PlanetDetailsModuleFactory(
    val exoplanetRepository: ExoplanetRepository
) {
    fun createPlanetDetailsViewModel(planetName: String): PlanetDetailsViewModel {
        return PlanetDetailsViewModel(exoplanetRepository, planetName)
    }
}
