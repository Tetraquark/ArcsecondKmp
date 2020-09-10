package ru.tetraquark.arcsecondkmp.model

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val coordinateSystem: CoordinateSystem? = null,
    val rightAscension: NumberParameter? = null,
    val declination: NumberParameter? = null,
    val epoch: Double = .0
)
