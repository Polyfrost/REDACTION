package net.wyvest.redaction.utils

object MathUtil {

    fun lerp(start: Float, end: Float, interpolation: Float): Float {
        return start + (end - start) * clamp01(interpolation)
    }

    private fun clamp01(value: Float): Float {
        if (value.toDouble() < 0.0) return 0.0f
        return if (value.toDouble() > 1.0) 1f else value
    }
    /**
     * Taken from Wynntils under GNU Affero General Public License v3.0
     * https://github.com/Wynntils/Wynntils/blob/development/LICENSE
     * @author Wynntils
     * @return par0 cast as an int, and no greater than Integer.MAX_VALUE-1024
     */
    fun fastFloor(value: Double) = (value + 1024.0).toInt() - 1024
}