package org.polyfrost.redaction.client.utils

object MathUtil {

    private fun clamp01(value: Float): Float {
        return if (value.toDouble() < 0.0) {
            0.0f
        } else if (value.toDouble() > 1.0) {
            1f
        } else {
            value
        }
    }

}
