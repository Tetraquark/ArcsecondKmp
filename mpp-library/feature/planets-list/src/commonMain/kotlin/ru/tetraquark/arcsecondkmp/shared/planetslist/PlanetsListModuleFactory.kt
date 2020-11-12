package ru.tetraquark.arcsecondkmp.shared.planetslist

class PlanetsListModuleFactory(
    val exoplanetRepository: ExoplanetRepository
) {
    fun createPlanetsListViewModel(): PlanetsListViewModel {
        return PlanetsListViewModel(exoplanetRepository = exoplanetRepository)
    }
}
