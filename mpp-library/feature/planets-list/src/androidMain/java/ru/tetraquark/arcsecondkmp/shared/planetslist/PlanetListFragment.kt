package ru.tetraquark.arcsecondkmp.shared.planetslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.icerock.moko.mvvm.createViewModelFactory
import kotlinx.coroutines.Job
import oolong.Dispatch
import ru.tetraquark.arcsecondkmp.model.Exoplanet

class PlanetListFragment : Fragment() {

    private var oolongJob: Job? = null

    private val viewModel by viewModels<PlanetsListViewModel>(
        factoryProducer = {
            createViewModelFactory {
                DepsProvider.planetsListModuleFactory.createPlanetsListViewModel()
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val composeView = ComposeView(requireContext())
        oolongJob = oolong.runtime(
            init = initFeature(DepsProvider.planetsListModuleFactory.exoplanetRepository),
            update = planetsListUpdate,
            view = planetsListView,
            render = { props, dispatch ->
                composeView.render(props, dispatch)
            }
        )

        return composeView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        oolongJob?.cancel()
    }

    private fun ComposeView.render(
        viewProperties: ViewProperties,
        dispatch: Dispatch<Message>
    ) {
        setContent {
            val screenState = viewProperties.planetsListState
            DepsProvider.appTheme {
                Column {
                    AppBar()
                    Surface(color = MaterialTheme.colors.background) {

                        when (screenState) {
                            is PlanetsListState.Loading -> {
                                LoadingState()
                            }
                            is PlanetsListState.Empty -> {
                                Text(text = "Empty list")
                            }
                            is PlanetsListState.Data -> {
                                Planets(planets = screenState.list) {
                                    dispatch(Message.RouteToPlanetDetails(it))
                                    //(activity as? PlanetsListRouter)?.navigateToPlanetDetails(it)
                                }
                            }
                            is PlanetsListState.Error -> {
                                Text(text = "Error: ${screenState.errorText}")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun LoadingState() {
        Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun AppBar() {
        TopAppBar(
            title = {
                Text(text = "Exoplanets")
            }
        )
    }

    @Composable
    fun Planets(planets: List<Exoplanet>, onPlanetClick: (String) -> Unit) {
        Stack {
            ScrollableColumn {
                planets.forEach {
                    PlanetCard(it, onPlanetClick)
                }
            }
        }
    }

    @Composable
    fun PlanetCard(planet: Exoplanet, onPlanetClick: (String) -> Unit) {
        Card(
            modifier = Modifier.padding(8.dp) + Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.clickable(
                    onClick = {
                        onPlanetClick(planet.name)
                    }
                ) + Modifier.padding(8.dp)
            ) {
                Text(
                    text = remember { "Name: ${planet.name}" },
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = remember { "Parent star: ${planet.parentStar}" },
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = remember { "Detection method: ${planet.detectionMethod}" },
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}
