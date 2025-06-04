package org.polyfrost.redaction.client.features.particles

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiContainer
import org.polyfrost.redaction.client.RedactionConfig
import org.polyfrost.redaction.features.particles.ParticleGenerator

object ParticleManager {

    var hasChanged = false
    private val particleGenerator = ParticleGenerator()

    fun initialize() {
        TODO("Awaiting OneConfig screen render event")
    }

    private fun renderParticles(screen: GuiScreen) {
        if (screen is GuiContainer && RedactionConfig.addSnow) {
            particleGenerator.draw(screen.width, screen.height)
        }
    }

}
