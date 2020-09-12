package ru.tetraquark.arcsecondkmp.shared.planetdetails

import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.tetraquark.arcsecondkmp.model.Exoplanet

class PlanetDetailsViewModel(
    private val exoplanetRepository: ExoplanetRepository,
    private val planetName: String
) : ViewModel() {

    private val _screenState = MutableLiveData<State<Exoplanet, Throwable>>(State.Loading())
    val screenState: LiveData<State<Exoplanet, Throwable>> = _screenState.readOnly()

    init {
        viewModelScope.reloadExoplanetInfo()
    }

    private fun CoroutineScope.reloadExoplanetInfo() {
        _screenState.value = State.Loading()
        launch {
            _screenState.value = try {
                State.Data(exoplanetRepository.getExoplanetInfo(planetName))
            } catch (cause: Throwable) {
                State.Error(cause)
            }
        }
    }
}
