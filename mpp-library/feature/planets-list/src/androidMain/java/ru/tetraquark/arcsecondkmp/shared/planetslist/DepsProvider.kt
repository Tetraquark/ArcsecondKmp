package ru.tetraquark.arcsecondkmp.shared.planetslist

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView

// TODO: Replace with DI
object DepsProvider {
    lateinit var planetsListModuleFactory: PlanetsListModuleFactory

    lateinit var appTheme: @Composable (@Composable () -> Unit) -> Unit
}
