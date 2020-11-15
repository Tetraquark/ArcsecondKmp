package ru.tetraquark.arcsecondkmp.shared.planetdetails

import oolong.effect
import oolong.effect.none
import oolong.init
import oolong.update
import oolong.view
import ru.tetraquark.arcsecondkmp.model.Exoplanet

sealed class PlanetsDetailsState {
    object Loading : PlanetsDetailsState()
    data class Error(val errorText: String) : PlanetsDetailsState()
    data class Data(val exoplanet: Exoplanet) : PlanetsDetailsState()
}

data class PlanetDetailsModel(
    val state: PlanetsDetailsState
)

sealed class Message {
    class LoadDetailsData(val planetName: String) : Message()
    data class LoadExoPlanetSuccess(val planet: Exoplanet) : Message()
    data class LoadExoPlanetException(val cause: Throwable) : Message()
    object RouteBack : Message()
}

class ViewProperties(
    val planetsDetailsState: PlanetsDetailsState
)

fun initFeature(repo: ExoplanetRepository, planetName: String) = init<PlanetDetailsModel, Message> {
    PlanetDetailsModel(PlanetsDetailsState.Loading) to loadExoplanetInfo(repo, planetName)
}


fun planetsListUpdate(repo: ExoplanetRepository) = update<PlanetDetailsModel, Message> { message, model ->
    when (message) {
        is Message.LoadDetailsData -> {
            model.copy(state = PlanetsDetailsState.Loading) to loadExoplanetInfo(repo, message.planetName)
        }
        is Message.LoadExoPlanetSuccess -> {
            model.copy(state = PlanetsDetailsState.Data(message.planet)) to none()
        }
        is Message.LoadExoPlanetException -> {
            model.copy(state = PlanetsDetailsState.Error(message.cause.toString())) to none()
        }
        is Message.RouteBack -> {
            TODO()
        }
    }
}

val planetDetailsView = view<PlanetDetailsModel, ViewProperties> {
    ViewProperties(planetsDetailsState = it.state)
}

fun loadExoplanetInfo(repo: ExoplanetRepository, planetName: String) = effect<Message> { dispatch ->
    try {
        dispatch(Message.LoadExoPlanetSuccess(repo.getExoplanetInfo(planetName)))
    } catch (cause: Throwable) {
        dispatch(Message.LoadExoPlanetException(cause))
    }
}
