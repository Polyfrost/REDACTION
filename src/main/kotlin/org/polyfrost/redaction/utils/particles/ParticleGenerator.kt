package org.polyfrost.redaction.utils.particles

import net.minecraft.client.Minecraft
import org.polyfrost.redaction.config.RedactionConfig
import org.polyfrost.redaction.features.ParticleManager
import org.polyfrost.redaction.utils.RenderUtils
import java.util.*


/**
 * Particle API This Api is free2use But u have to mention me.
 *
 * @author Vitox
 * @version 3.0
 */
class ParticleGenerator {
    private val particles = ArrayList<Particle>()
    private var prevWidth = 0
    private var prevHeight = 0
    fun draw(mouseX: Int, mouseY: Int) {
        if (particles.isEmpty() || prevWidth != Minecraft.getMinecraft().displayWidth || prevHeight != Minecraft.getMinecraft().displayHeight || ParticleManager.hasChanged) {
            particles.clear()
            create()
        }
        prevWidth = Minecraft.getMinecraft().displayWidth
        prevHeight = Minecraft.getMinecraft().displayHeight
        for (particle in particles) {
            particle.fall()
            particle.interpolation()
            val range = 50
            val mouseOver =
                mouseX >= particle.x - range && mouseY >= particle.y - range && mouseX <= particle.x + range && mouseY <= particle.y + range
            if (mouseOver) {
                particles.filter { part: Particle ->
                        (part.x > particle.x && part.x - particle.x < range && particle.x - part.x < range
                                && (part.y > particle.y && part.y - particle.y < range
                                || particle.y > part.y && particle.y - part.y < range))
                    }.forEach { connectable: Particle ->
                        particle.connect(
                            connectable.x,
                            connectable.y
                        )
                    }
            }
            RenderUtils.drawCircle(particle.x, particle.y, particle.size, RedactionConfig.snowColor.rgb)
        }
    }

    private fun create() {
        val random = Random()
        for (i in 0 until RedactionConfig.particles) particles.add(
            Particle(
                random.nextInt(Minecraft.getMinecraft().displayWidth).toFloat(),
                random.nextInt(Minecraft.getMinecraft().displayHeight).toFloat()
            )
        )
        if (ParticleManager.hasChanged) {
            ParticleManager.hasChanged = false
        }
    }
}