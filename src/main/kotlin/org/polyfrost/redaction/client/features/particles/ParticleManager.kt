package org.polyfrost.redaction.client.features.particles

import dev.deftu.eventbus.on
import dev.deftu.omnicore.api.client.events.ScreenEvent
import dev.deftu.omnicore.api.client.input.OmniMouse
import dev.deftu.omnicore.api.client.render.ImmediateScreenRenderer
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.client.render.OmniResolution
import dev.deftu.omnicore.api.eventBus
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiContainer
import org.polyfrost.redaction.client.RedactionConfig
import kotlin.random.Random

object ParticleManager {
    private val RANDOM = Random.Default

    private val currentParticles = mutableListOf<Particle>()
    private var lastWidth = -1
    private var lastHeight = -1

    fun initialize() {
        eventBus.on<ScreenEvent.Render.Post> {
            ImmediateScreenRenderer.render(context) {
                renderParticles(context, screen)
            }
        }
    }

    fun updateParticles() {
        val width = OmniResolution.scaledWidth
        val height = OmniResolution.scaledHeight

        currentParticles.clear()
        repeat(RedactionConfig.particles) {
            currentParticles += Particle(
                random = RANDOM,
                initialX = RANDOM.nextInt(width).toFloat(),
                initialY = RANDOM.nextInt(height).toFloat()
            )
        }

        println("Initialized ${currentParticles.size} particles for resolution ${width}x${height}")

        lastWidth = width
        lastHeight = height
    }

    private fun renderParticles(context: OmniRenderingContext, screen: GuiScreen) {
        if (
            !RedactionConfig.addSnow ||
            //#if MC >= 1.16.5
            //$$ screen !is AbstractContainerScreen<*>
            //#else
            screen !is GuiContainer
            //#endif
        ) {
            return
        }

        if (currentParticles.isEmpty() || lastWidth != OmniResolution.scaledWidth || lastHeight != OmniResolution.scaledHeight) {
            updateParticles()
        }

        for (particle in currentParticles) {
            particle.update()
        }

        val mouseX = OmniMouse.scaledX.toInt()
        val mouseY = OmniMouse.scaledY.toInt()
        if (RedactionConfig.connectSnow) {
            connectParticles(context, currentParticles, mouseX, mouseY)
        }

        drawParticles(context, currentParticles)
    }
}
