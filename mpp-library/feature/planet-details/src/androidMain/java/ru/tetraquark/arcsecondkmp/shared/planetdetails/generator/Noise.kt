package ru.tetraquark.arcsecondkmp.shared.planetdetails.generator

inline class Noise(val seed: Int) {

    fun linearInterpolate(a: Float, b: Float, x: Float): Float {
        return a * (1 - x) + b * x
    }

    fun noise(x: Int, y: Int): Float {
        var n = x + y * 57 + seed
        n = n shl 13 xor n
        return 1 - (n * (n * n * 15731 + 789221) + 1376312589 and 0x7fffffff) / 1073741824f
    }

    fun smoothNoise(x: Int, y: Int): Float {
        val nodes = (noise(x + 1, y + 1) + noise(x + 1, y - 1)
                + noise(x - 1, y + 1) + noise(x - 1, y - 1)) / 16f
        val edges = (noise(x, y + 1) + noise(x, y - 1) + noise(x - 1, y)
                + noise(x + 1, y)) / 8f
        val center = noise(x, y) / 4f
        return nodes + edges + center
    }

    fun interpolatedNoise(x: Float, y: Float): Float {
        val intX = x.toInt()
        val intX1 = x - intX
        val intY = y.toInt()
        val intY1 = y - intY

        val sn1 = smoothNoise(intX, intY)
        val sn2 = smoothNoise(intX + 1, intY)
        val sn3 = smoothNoise(intX, intY + 1)
        val sn4 = smoothNoise(intX + 1, intY + 1)
        val lin1 = linearInterpolate(sn1, sn2, intX1)
        val lin2 = linearInterpolate(sn3, sn4, intX1)
        return linearInterpolate(lin1, lin2, intY1)
    }
}
