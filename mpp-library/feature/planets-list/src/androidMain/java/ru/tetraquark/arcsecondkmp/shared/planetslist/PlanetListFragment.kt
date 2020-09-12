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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.createViewModelFactory
import ru.tetraquark.arcsecondkmp.model.Exoplanet

class PlanetListFragment : Fragment() {

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
        return ComposeView(requireContext()).apply {
            setContent {
                DepsProvider.appTheme {
                    Column {
                        AppBar()
                        Surface(color = MaterialTheme.colors.background) {
                            when(val state = viewModel.screenState.ld().observeAsState().value) {
                                is State.Data -> {
                                    Planets(planets = state.data) {
                                        (activity as? PlanetsListRouter)?.navigateToPlanetDetails(it)
                                    }
                                }
                                is State.Empty -> { Text(text = "Empty list") }
                                is State.Loading -> { LoadingState() }
                                is State.Error -> {
                                    Text(text = "Error")
                                    state.error.printStackTrace()
                                }
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
