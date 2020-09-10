package ru.tetraquark.arcsecondkmp.model

import kotlinx.serialization.Serializable

@Serializable
data class Exoplanet(
    val name: String,
    val parentStar: String,
    val detectionMethod: String,
    val massDetectionMethod: String,
    val radiusDetectionMethod: String,
    val coordinates: Coordinates,
    val mass: NumberParameter?,
    val radius: NumberParameter?,
    val inclination: NumberParameter?,
    val eccentricity: Double?,
    val orbitalPeriod: NumberParameter?,
    val calculatedTemperature: NumberParameter?,
    val surfaceGravity: NumberParameter?
)
