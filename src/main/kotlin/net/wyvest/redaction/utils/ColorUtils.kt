package net.wyvest.redaction.utils

import java.awt.Color

object ColorUtils {

    /**
     * @return A changing colour based on the users' computer time. Simulates a "chroma" colour.
     */
    @JvmStatic
    fun timeBasedChroma(): Int {
        val l = System.currentTimeMillis()
        return Color.HSBtoRGB(l % 2000L / 2000.0f, 1.0f, 1.0f)
    }

    /**
     * @return The red value of the provided RGBA value.
     */
    @JvmStatic
    fun getRed(rgba: Int): Int {
        return (rgba shr 16) and 0xFF
    }

    /**
     * @return The green value of the provided RGBA value.
     */
    @JvmStatic
    fun getGreen(rgba: Int): Int {
        return (rgba shr 8) and 0xFF
    }

    /**
     * @return The blue value of the provided RGBA value.
     */
    @JvmStatic
    fun getBlue(rgba: Int): Int {
        return (rgba shr 0) and 0xFF
    }

    /**
     * @return The alpha value of the provided RGBA value.
     */
    @JvmStatic
    fun getAlpha(rgba: Int): Int {
        return (rgba shr 24) and 0xFF
    }
}