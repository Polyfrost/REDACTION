package org.polyfrost.redaction.client.features.particles

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import org.polyfrost.redaction.client.RedactionConfig
import kotlin.random.Random

// TODO: don't use omnicore
object ParticleManager {
    private val RANDOM = Random.Default

    private val currentParticles = mutableListOf<Particle>()
    private var lastWidth = -1
    private var lastHeight = -1

    fun initialize() {
        ScreenEvents.BEFORE_INIT.register { _, screen, _, _ ->
            ScreenEvents.beforeRender(screen).register { _, guiGraphics, _, _, _ ->
//                val ctx = OmniRenderingContext.from(guiGraphics)
//                ImmediateScreenRenderer.render(ctx) {
//                    renderParticles(ctx, screen)
//                }
            }
        }
    }

    fun updateParticles() {
        val width = mc.window.guiScaledWidth
        val height = mc.window.guiScaledHeight

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

    private fun renderParticles(graphics: GuiGraphics, screen: Screen) {
        if (!RedactionConfig.addSnow || screen !is AbstractContainerScreen<*>) {
            return
        }

        if (currentParticles.isEmpty() || lastWidth != graphics.guiWidth() || lastHeight != graphics.guiHeight()) {
            updateParticles()
        }

        for (particle in currentParticles) {
            particle.update()
        }

        val mouseX = mc.mouseHandler.xpos() * (graphics.guiWidth() / mc.window.width)
        val mouseY = mc.mouseHandler.ypos() * (graphics.guiHeight() / mc.window.height)
        if (RedactionConfig.connectSnow) {
            connectParticles(graphics, currentParticles, mouseX.toInt(), mouseY.toInt())
        }

        drawParticles(graphics, currentParticles)
    }
}
