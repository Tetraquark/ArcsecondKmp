package ru.tetraquark.arcsecondkmp.shared.planetdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import ru.tetraquark.arcsecondkmp.model.Exoplanet
import ru.tetraquark.arcsecondkmp.model.NumberParameter
import ru.tetraquark.arcsecondkmp.shared.planetdetails.generator.PlanetImageGenerator

class PlanetDetailsFragment : Fragment() {

    private val viewModel by viewModels<PlanetDetailsViewModel>(
        factoryProducer = {
            createViewModelFactory {
                val planetName =  arguments?.getParcelable<DetailsArgs>(DetailsArgs.ARGS_KEY)?.planetName
                    ?: throw IllegalArgumentException("Arguments can't be empty.")

                DepsProvider.planetDetailsModuleFactory.createPlanetDetailsViewModel(planetName)
            }
        }
    )

    private val planetImageGenerator = PlanetImageGenerator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                DepsProvider.appTheme {
                    Column {
                        Surface(color = MaterialTheme.colors.background) {
                            when(val state = viewModel.screenState.ld().observeAsState().value) {
                                is State.Data -> {
                                    ExoplanetDetails(state.data) {
                                        (activity as? PlanetDetailsRouter)?.navigateBack()
                                    }
                                }
                                is State.Empty -> { Text(text = "Empty") }
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
    private fun LoadingState() {
        Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
            CircularProgressIndicator()
        }
    }

    @Composable
    private fun ExoplanetDetails(
        exoplanet: Exoplanet,
        onBackClick: () -> Unit
    ) {
        Scaffold(
            topBar = {
                AppBar(exoplanet.name, onBackClick)
            },
            bodyContent = { innerPadding ->
                val modifier = Modifier.padding(innerPadding)

                ScrollableColumn(
                    modifier = modifier.padding(horizontal = 16.dp)
                ) {
                    val image = planetImageGenerator.generatePlanetBitmap(
                        width = 128,
                        height = 128,
                        seed = exoplanet.name.hashCode()
                    ).asImageAsset()
                    val imageModifier = Modifier
                        .preferredHeightIn(minHeight = 256.dp, maxHeight = 256.dp)
                        .preferredWidthIn(minWidth = 256.dp, maxWidth = 256.dp)
                        .fillMaxWidth()
                        .gravity(Alignment.CenterHorizontally)
                        .clip(shape = MaterialTheme.shapes.medium)
                    Image(image, modifier = imageModifier, contentScale = ContentScale.Crop)

                    exoplanet.coordinates.coordinateSystem?.let {
                        RowField(title = "Coordinate system", value = it.title)
                    }
                    exoplanet.coordinates.declination?.let {
                        RowField(title = "Declination", value = "${it.value} ${it.unit}")
                    }
                    exoplanet.coordinates.rightAscension?.let {
                        RowField(title = "Right ascension", value = "${it.value} ${it.unit}")
                    }
                    RowField(title = "Epoch", value = "${exoplanet.coordinates.epoch}")

                    RowField(title = "Parent star", value = exoplanet.parentStar)
                    RowField(title = "Detection method", value = exoplanet.detectionMethod)
                    RowField(title = "Mass detection method", value = exoplanet.massDetectionMethod)
                    RowField(
                        title = "Radius detection method",
                        value = exoplanet.massDetectionMethod
                    )
                    exoplanet.eccentricity?.let {
                        RowField(title = "Eccentricity", value = it.toString())
                    }

                    exoplanet.mass?.ParameterRowField(title = "Mass")
                    exoplanet.radius?.ParameterRowField(title = "Radius")
                    exoplanet.inclination?.ParameterRowField(title = "Inclination")
                    exoplanet.orbitalPeriod?.ParameterRowField(title = "Orbital period")
                    exoplanet.calculatedTemperature?.ParameterRowField(title = "Calculated temperature")
                    exoplanet.surfaceGravity?.ParameterRowField(title = "Surface gravity")
                }
            }
        )
    }

    @Composable
    private fun NumberParameter.ParameterRowField(title: String) {
        RowField(title = title, value = "$value $unit")
    }

    @Composable
    private fun RowField(title: String, value: String) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
            Divider()
            ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
                Text(
                    text = title,
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.caption
                )
            }
            ProvideEmphasis(emphasis = EmphasisAmbient.current.high) {
                Text(
                    text = value,
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }

    @Composable
    private fun AppBar(title: String, onBackClick: () -> Unit) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle2,
                    color = contentColor()
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Filled.ArrowBack)
                }
            }
        )
    }

    @Parcelize
    data class DetailsArgs(val planetName: String) : Parcelable {
        companion object {
            const val ARGS_KEY = "args"
        }
    }
}
