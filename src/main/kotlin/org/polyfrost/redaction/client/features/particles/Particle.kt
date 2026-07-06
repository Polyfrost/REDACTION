package org.polyfrost.redaction.client.features.particles

import org.polyfrost.oneconfig.utils.v1.dsl.mc
import kotlin.random.Random

class Particle(
    random: Random,
    initialX: Float,
    initialY: Float
) {
    companion object {
        // Akrz: 1/2 side length of square area around the cursor to connect particles in
        const val CONNECT_RANGE = 50
    }

    var x: Float = initialX
        private set
    var y: Float = initialY
        private set

    private val ySpeed = random.nextFloat() * 0.5f
    private val xSpeed = random.nextFloat() * 0.5f

    val size: Float = 0.3f + random.nextFloat() * 1.3f

    fun update() {
        y += ySpeed
        x += xSpeed

        when {
            x > mc.window.guiScaledWidth -> x = 1f
            x < 1 -> x = mc.window.guiScaledWidth.toFloat()

            y > mc.window.guiScaledHeight -> y = 1f
            y < 1 -> y = mc.window.guiScaledHeight.toFloat()
        }
    }

    fun isMouseOver(mouseX: Int, mouseY: Int): Boolean {
        return mouseX in (x - CONNECT_RANGE).toInt()..(x + CONNECT_RANGE).toInt() &&
                mouseY in (y - CONNECT_RANGE).toInt()..(y + CONNECT_RANGE).toInt()
    }
}
