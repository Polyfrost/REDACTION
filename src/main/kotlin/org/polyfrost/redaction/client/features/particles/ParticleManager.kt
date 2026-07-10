package org.polyfrost.redaction.client.features.particles

//? if >=26.2 {
import com.mojang.blaze3d.PrimitiveTopology
//?} else
//import com.mojang.blaze3d.vertex.VertexFormat

//? if >=1.21.8 {
import org.polyfrost.redaction.client.features.particles.render.ParticleConnectionRenderState
import org.polyfrost.redaction.client.features.particles.render.ParticleRenderState
//?} else
//import net.minecraft.client.renderer.RenderType

//? if >=1.21.5 {
import com.mojang.blaze3d.pipeline.RenderPipeline
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import org.polyfrost.redaction.RedactionConstants
//?} else
//import net.minecraft.client.renderer.RenderStateShard

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import org.polyfrost.redaction.client.RedactionConfig
import kotlin.random.Random

object ParticleManager {
    private val currentParticles = mutableListOf<Particle>()
    private var lastWidth = -1
    private var lastHeight = -1

    //? if >=1.21.5 {
    val PARTICLE_PIPELINE by lazy {
        RenderPipelines.register(RenderPipeline.builder(RenderPipelines.GUI_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(RedactionConstants.ID, "particles"))
            //? if >=26.2 {
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
            //?} else
            //.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES)
            .withVertexShader("core/position_color")
            .withFragmentShader("core/position_color")
            .build())
    }

    val PARTICLE_CONNECTION_PIPELINE by lazy {
        RenderPipelines.register(RenderPipeline.builder(RenderPipelines.GUI_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(RedactionConstants.ID, "particle_connections"))
            //? if >=26.2 {
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            //?} else
            //.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
            .withVertexShader("core/position_color")
            .withFragmentShader("core/position_color")
            .withCull(false)
            .build())
    }
    //?}

    //? if <1.21.8 {
    /*private val PARTICLE by lazy {
        RenderType.create(
            "redaction_particles",
            //? if <=1.21.4 {
            /*DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.TRIANGLES,
            *///?}
            1536,
            //? if >=1.21.5
            PARTICLE_PIPELINE,
            RenderType.CompositeState.builder()
                //? if <=1.21.4
                //.setShaderState(RenderStateShard.RENDERTYPE_GUI_SHADER)
                .createCompositeState(false)
        )
    }

    private val PARTICLE_CONNECTIONS by lazy {
        RenderType.create(
            "redaction_particle_connections",
            //? if <=1.21.4 {
            /*DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            *///?}
            1536,
            //? if >=1.21.5
            PARTICLE_CONNECTION_PIPELINE,
            RenderType.CompositeState.builder()
                //? if <=1.21.4 {
                /*.setShaderState(RenderStateShard.RENDERTYPE_GUI_SHADER)
                .setCullState(RenderStateShard.NO_CULL)
                *///?}
                .createCompositeState(false)
        )
    }
    *///?}

    fun initialize() {
        ScreenEvents.BEFORE_INIT.register { _, screen, _, _ ->
            //~ if <26.1 'beforeExtract' -> 'beforeRender'
            ScreenEvents.beforeExtract(screen).register { _, graphics, _, _, _ ->
                renderParticles(graphics, screen)
            }
        }
    }

    fun updateParticles() {
        val width = mc.window.guiScaledWidth
        val height = mc.window.guiScaledHeight

        currentParticles.clear()
        repeat(RedactionConfig.particles) {
            currentParticles += Particle(
                initialX = Random.nextInt(width).toFloat(),
                initialY = Random.nextInt(height).toFloat()
            )
        }

        println("Initialized ${currentParticles.size} particles for resolution ${width}x${height}")

        lastWidth = width
        lastHeight = height
    }

    private fun renderParticles(graphics: GuiGraphicsExtractor, screen: Screen) {
        if (!RedactionConfig.addSnow || screen !is AbstractContainerScreen<*>) {
            return
        }

        if (currentParticles.isEmpty() || lastWidth != graphics.guiWidth() || lastHeight != graphics.guiHeight()) {
            updateParticles()
        }

        for (particle in currentParticles) {
            particle.update()
        }

        if (RedactionConfig.connectSnow) {
            val mouseX = mc.mouseHandler.xpos() * (graphics.guiWidth().toDouble() / mc.window.width)
            val mouseY = mc.mouseHandler.ypos() * (graphics.guiHeight().toDouble() / mc.window.height)

            //? if >=1.21.8 {
            //~ if <26.1 'addGuiElement' -> 'submitGuiElement'
            graphics.guiRenderState.addGuiElement(
                ParticleConnectionRenderState(
                    graphics.pose(),
                    currentParticles,
                    mouseX.toInt(),
                    mouseY.toInt()
                )
            )
            //?} else {
            /*val buffer = mc.renderBuffers().bufferSource().getBuffer(PARTICLE_CONNECTIONS)
            ParticleRenderer.connectParticles(
                buffer,
                graphics.pose(),
                currentParticles,
                mouseX.toInt(),
                mouseY.toInt()
            )
            *///?}
        }

        //? if >=1.21.8 {
        //~ if <26.1 'addGuiElement' -> 'submitGuiElement'
        graphics.guiRenderState.addGuiElement(
            ParticleRenderState(
                graphics.pose(),
                currentParticles
            )
        )
        //?} else {
        /*val buffer = mc.renderBuffers().bufferSource().getBuffer(PARTICLE)
        ParticleRenderer.drawParticles(
            buffer,
            graphics.pose(),
            currentParticles
        )
        *///?}
    }
}
