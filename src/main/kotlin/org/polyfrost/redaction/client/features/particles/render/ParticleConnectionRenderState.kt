package org.polyfrost.redaction.client.features.particles.render

//? if >=1.21.8 {
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.TextureSetup
//~ if <26.1 'renderer.state.gui' -> 'gui.render.state'
import net.minecraft.client.renderer.state.gui.GuiElementRenderState
import org.joml.Matrix3x2fc
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import org.polyfrost.redaction.client.features.particles.Particle
import org.polyfrost.redaction.client.features.particles.ParticleManager
import org.polyfrost.redaction.client.features.particles.ParticleRenderer

class ParticleConnectionRenderState(
    val pose: Matrix3x2fc,
    val particles: Collection<Particle>,
    val mouseX: Int,
    val mouseY: Int
) : GuiElementRenderState {
    override fun buildVertices(vertexConsumer: VertexConsumer, /*? if 1.21.8 {*/ /*f: Float *//*?}*/) {
        ParticleRenderer.connectParticles(vertexConsumer, pose, particles, mouseX, mouseY)
    }

    override fun pipeline() = ParticleManager.PARTICLE_CONNECTION_PIPELINE
    override fun textureSetup() = TextureSetup.noTexture()
    override fun scissorArea() = null
    override fun bounds() = ScreenRectangle(0, 0, mc.window.guiScaledWidth, mc.window.guiScaledHeight)
}
//?}
