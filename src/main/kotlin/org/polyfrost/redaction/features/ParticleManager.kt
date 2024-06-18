package org.polyfrost.redaction.features

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.polyfrost.redaction.config.RedactionConfig
import org.polyfrost.redaction.utils.particles.ParticleGenerator

object ParticleManager {
    var hasChanged = false
    private val particleGenerator = ParticleGenerator()

    @SubscribeEvent
    fun render(event: GuiScreenEvent.DrawScreenEvent.Pre) {
        if (event.gui is GuiContainer && RedactionConfig.addSnow) {
            particleGenerator.draw(event.mouseX, event.mouseY)
        }
    }
}