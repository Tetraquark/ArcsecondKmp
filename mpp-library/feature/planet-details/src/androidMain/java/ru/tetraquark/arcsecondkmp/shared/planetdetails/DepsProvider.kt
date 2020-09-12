package ru.tetraquark.arcsecondkmp.shared.planetdetails

import androidx.compose.runtime.Composable

// TODO: Replace with DI
object DepsProvider {
    lateinit var planetDetailsModuleFactory: PlanetDetailsModuleFactory

    lateinit var appTheme: @Composable (@Composable () -> Unit) -> Unit
}
