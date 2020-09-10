package ru.tetraquark.arcsecondkmp.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckResult(
    val isValid: Boolean
)
