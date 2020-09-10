package ru.tetraquark.arcsecondkmp.shared.planetslist

import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.tetraquark.arcsecondkmp.model.Exoplanet

class PlanetsListViewModel(
    private val exoplanetRepository: ExoplanetRepository
) : ViewModel() {

    private val _screenState = MutableLiveData<State<List<Exoplanet>, Throwable>>(State.Loading())
    val screenState: LiveData<State<List<Exoplanet>, Throwable>> = _screenState.readOnly()

    init {
        viewModelScope.loadExoplanets()
    }

    private fun CoroutineScope.loadExoplanets() {
        _screenState.value = State.Loading()
        launch {
            _screenState.value = try {
                val list = exoplanetRepository.loadExoplanets()

                if (list.isEmpty()) State.Empty() else State.Data(list)
            } catch (cause: Throwable) {
                State.Error(cause)
            }
        }
    }
}
