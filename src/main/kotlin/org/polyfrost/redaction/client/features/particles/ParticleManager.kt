package org.polyfrost.redaction.client.features.particles

import dev.deftu.omnicore.api.client.input.OmniMouse
import dev.deftu.omnicore.api.client.render.ImmediateScreenRenderer
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.client.render.OmniResolution
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import org.polyfrost.redaction.client.RedactionConfig
import kotlin.random.Random

object ParticleManager {
    private val RANDOM = Random.Default

    private val currentParticles = mutableListOf<Particle>()
    private var lastWidth = -1
    private var lastHeight = -1

    fun initialize() {
        ScreenEvents.BEFORE_INIT.register { _, screen, _, _ ->
            ScreenEvents.beforeRender(screen).register { _, guiGraphics, _, _, _ ->
                val ctx = OmniRenderingContext.from(guiGraphics)
                ImmediateScreenRenderer.render(ctx) {
                    renderParticles(ctx, screen)
                }
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

    private fun renderParticles(context: OmniRenderingContext, screen: Screen) {
        if (!RedactionConfig.addSnow || screen !is AbstractContainerScreen<*>) {
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
