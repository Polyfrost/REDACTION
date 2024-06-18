package org.polyfrost.redaction.utils.particles

import cc.polyfrost.oneconfig.libs.universal.UResolution
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.polyfrost.redaction.utils.RenderUtils
import java.util.*


/**
 * Particle API
 * This Api is free2use
 * But u have to mention me.
 *
 * @author Vitox
 * @version 3.0
 */
@SideOnly(Side.CLIENT)
class Particle internal constructor(var x: Float, var y: Float) {
    val size: Float
    private val ySpeed = Random().nextInt(5).toFloat()
    private val xSpeed = Random().nextInt(5).toFloat()

    init {
        size = genRandom()
    }

    private fun lint1(f: Float): Float {
        return 1.02.toFloat() * (1.0f - f) + f
    }

    private fun lint2(f: Float): Float {
        return 1.02.toFloat() + f * (1.0.toFloat() - 1.02.toFloat())
    }

    fun connect(x: Float, y: Float) {
        RenderUtils.connectPoints(x, y, x, y)
    }

    fun interpolation() {
        for (n in 0..64) {
            val f = n / 64.0f
            val p1 = lint1(f)
            val p2 = lint2(f)
            if (p1 != p2) {
                y -= f
                x -= f
            }
        }
    }

    fun fall() {
        val mc = Minecraft.getMinecraft()
        y += ySpeed
        x += xSpeed
        if (y > mc.displayHeight) {
            y = 1f
        }
        if (x > mc.displayWidth) {
            x = 1f
        }
        if (x < 1) {
            x = UResolution.scaledWidth.toFloat()
        }
        if (y < 1) {
            y = UResolution.scaledHeight.toFloat()
        }
    }

    private fun genRandom(): Float {
        return (0.3f + Math.random() * 1.3f).toFloat()
    }
}