package ru.tetraquark.arcsecondkmp.shared.planetslist

import oolong.effect
import oolong.effect.none
import oolong.init
import oolong.update
import oolong.view
import ru.tetraquark.arcsecondkmp.model.Exoplanet

private var exoplanetRepository: ExoplanetRepository? = null

sealed class PlanetsListState {
    object Loading : PlanetsListState()
    object Empty : PlanetsListState()
    data class Error(val errorText: String) : PlanetsListState()
    data class Data(val list: List<Exoplanet>) : PlanetsListState()
}

data class PlanetsListModel(
    val listState: PlanetsListState
)

sealed class Message {
    object LoadPlanets : Message()
    data class LoadPlanetsSuccess(val list: List<Exoplanet>) : Message()
    data class LoadPlanetsException(val cause: Throwable) : Message()
    class RouteToPlanetDetails(val planetName: String) : Message()
}

class ViewProperties(
    val planetsListState: PlanetsListState,
    val refreshList: () -> Message
)

fun initFeature(_exoplanetRepository: ExoplanetRepository) = init<PlanetsListModel, Message> {
    exoplanetRepository = _exoplanetRepository
    PlanetsListModel(PlanetsListState.Loading) to loadExoplanets
}

val planetsListUpdate = update<PlanetsListModel, Message> { message, model ->
    when (message) {
        is Message.LoadPlanets -> {
            model.copy(listState = PlanetsListState.Loading) to loadExoplanets
        }
        is Message.LoadPlanetsSuccess -> {
            if (message.list.isEmpty()) {
                model.copy(listState = PlanetsListState.Empty) to none()
            } else {
                model.copy(listState = PlanetsListState.Data(message.list)) to none()
            }
        }
        is Message.LoadPlanetsException -> {
            model.copy(listState = PlanetsListState.Error(message.cause.toString())) to none()
        }
        is Message.RouteToPlanetDetails -> {
            TODO()
        }
    }
}

val planetsListView = view<PlanetsListModel, ViewProperties> {
    ViewProperties(
        planetsListState = it.listState,
        refreshList = { Message.LoadPlanets }
    )
}

val loadExoplanets = effect<Message> { dispatch ->
    try {
        exoplanetRepository?.loadExoplanets()?.let {
            dispatch(Message.LoadPlanetsSuccess(it))
        }
    } catch (cause: Throwable) {
        dispatch(Message.LoadPlanetsException(cause))
    }
}


