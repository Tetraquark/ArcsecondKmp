package ru.tetraquark.arcsecondkmp.model

import kotlinx.serialization.Serializable

@Serializable
data class NumberParameter(
    val value: Double,
    val unit: String
)

fun NumberParameter(value: Double?, unit: String?): NumberParameter? {
    if (value == null || unit == null) return null
    return NumberParameter(value, unit)
}
