package ru.tetraquark.arcsecondkmp.shared.planetslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.createViewModelFactory
import ru.tetraquark.arcsecondkmp.model.Exoplanet
//import ru.tetraquark.arcsecondkmp.shared.planetslist.generator.generatePlanetBitmap

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
                                is State.Data -> { Planets(planets = state.data) }
                                is State.Empty -> { Text(text = "Empty list") }
                                is State.Loading -> { Text(text = "loading") }
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
    fun AppBar() {
        TopAppBar(
            title = {
                Text(text = "Exoplanets")
            }
        )
    }

    @Composable
    fun Planets(planets: List<Exoplanet>) {
        Stack {
            ScrollableColumn {
                planets.forEach {
                    PlanetCard(planet = it)
                }
            }
        }
    }

    @Composable
    fun PlanetCard(planet: Exoplanet) {
        Card(
            modifier = Modifier.padding(8.dp) + Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.clickable(
                    onClick = {
                        // TODO: onClick
                    }
                ) + Modifier.padding(8.dp)
            ) {

/*
                val image = generatePlanetBitmap(256, 256).asImageAsset()
                val imageModifier = Modifier
                    .preferredHeightIn(minHeight = 256.dp, maxHeight = 256.dp)
                    .preferredWidthIn(minWidth = 256.dp, maxWidth = 256.dp)
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.medium)
                Image(image, modifier = imageModifier, contentScale = ContentScale.Crop)
*/

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
