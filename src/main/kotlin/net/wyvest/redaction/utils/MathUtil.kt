package net.wyvest.redaction.utils

object MathUtil {

    fun lerp(start: Float, end: Float, interpolation: Float): Float {
        return start + (end - start) * clamp01(interpolation)
    }

    private fun clamp01(value: Float): Float {
        if (value.toDouble() < 0.0) return 0.0f
        return if (value.toDouble() > 1.0) 1f else value
    }

    fun getRed(rgba: Int): Int {
        return rgba shr 16 and 255
    }

    fun getGreen(rgba: Int): Int {
        return rgba shr 8 and 255
    }

    fun getBlue(rgba: Int): Int {
        return rgba and 255
    }
}