package ru.tetraquark.arcsecondkmp.shared.planetdetails.generator

import android.graphics.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.math.pow
import kotlin.random.Random

private typealias Vector2 = Pair<Float, Float>

class PlanetImageGenerator {

    private val basePlanetsColors by lazy {
        arrayOf(
            Color(0x0D, 0x59, 0x63).toArgb(),
            Color(0x18, 0x3B, 0xA7).toArgb(),
            Color(0x09, 0x61, 0x46).toArgb(),
            Color(0x7A, 0x52, 0x07).toArgb()
        )
    }

    private val colorCoeff = basePlanetsColors.size / Int.MAX_VALUE
    private val basePlanetColor = Color(0x18, 0x3B, 0xA7).toArgb()

    fun generatePlanetBitmap(
        width: Int = 256,
        height: Int = 256,
        seed: Int = Random.nextInt()
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val halfWidth = (width / 2).toFloat()
        val halfHeight = (height / 2).toFloat()
        val radius = if (halfWidth > halfHeight) halfHeight - 2 else halfWidth - 2
        val center = Vector2(halfWidth, halfHeight)

        val planetBaseColorIndex: Int = seed * colorCoeff

        val paint = Paint().apply {
            color = basePlanetColor//basePlanetsColors[planetBaseColorIndex]
            flags = Paint.ANTI_ALIAS_FLAG
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawCircle(halfWidth, halfHeight, radius, paint)

        val perlinNoise = Noise(seed)
        var x = 0
        while (x < width) {
            var y = 0
            while (y < height) {
                val point = Vector2(x.toFloat(), y.toFloat())
                if (isInsideCircle(point, center, radius)) {
                    val nx = x * 0.25f
                    val ny = y * 0.25f
                    var noise = perlinNoise.interpolatedNoise(nx, ny)
                    noise += 0.5f * perlinNoise.interpolatedNoise(2 * nx, 2 * ny)
                    noise += 0.25f * perlinNoise.interpolatedNoise(4 * nx, 4 * ny)

                    biomColor(noise)?.let { noiseColor ->
                        val p = Paint().apply {
                            color = noiseColor
                            flags = Paint.ANTI_ALIAS_FLAG
                        }
                        canvas.drawPoint(point.first, point.second, p)
                    }
                }
                y++
            }
            x++
        }
        return bitmap
    }

    private fun biomColor(noise: Float): Int? = when {
        noise > 0.3 -> {
            Color.White.toArgb()
        }
        noise > 0.10 -> {
            Color(0xFF, 0xFF, 0xFF, 0xC8).toArgb()
        }
        noise > 0.0 -> {
            Color(0xFF, 0xFF, 0xFF, 0x80).toArgb()
        }
        else -> {
            null
        }
    }

    private fun isInsideCircle(point: Vector2, center: Vector2, r: Float): Boolean {
        return (point.first - center.first).pow(2) + (point.second - center.second).pow(2) < r.pow(2)
    }
}
